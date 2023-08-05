package com.example.honjarang.domain.user.service;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryDto;
import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryListDto;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.jointdelivery.exception.MenuNotFoundException;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryCartRepository;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryRepository;
import com.example.honjarang.domain.jointdelivery.repository.MenuRepository;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.exception.PaymentException;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.dto.LoginDto;
import com.example.honjarang.domain.user.dto.PointChargeDto;
import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.dto.UserInfoDto;
import com.example.honjarang.domain.user.entity.EmailVerification;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.DuplicateNicknameException;
import com.example.honjarang.domain.user.exception.EmailNotVerifiedException;
import com.example.honjarang.domain.user.exception.PasswordMismatchException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.EmailVerificationRepository;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PostService postService;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String USER_FCM_PREFIX = "fcm-token::";

    private final JointDeliveryRepository jointDeliveryRepository;

    private final JointDeliveryCartRepository jointDeliveryCartRepository;

    private final MenuRepository menuRepository;

    private final S3UploadService s3UploadService;

    @Transactional(readOnly = true)
    public User login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    public void addFcmToken(User loginUser, String fcmToken) {
        if (redisTemplate.opsForSet().size(USER_FCM_PREFIX + loginUser.getId()) == 0) {
            redisTemplate.opsForSet().add(USER_FCM_PREFIX + loginUser.getId(), fcmToken);
            redisTemplate.expire(USER_FCM_PREFIX + loginUser.getId(), 7, TimeUnit.DAYS);
        } else {
            redisTemplate.opsForSet().add(USER_FCM_PREFIX + loginUser.getId(), fcmToken);
        }
    }

    public Set<String> getFcmTokenList(User user) {
        return redisTemplate.opsForSet().members(USER_FCM_PREFIX + user.getId());
    }

    public void deleteFcmToken(User user, String fcmToken) {
        redisTemplate.opsForSet().remove(USER_FCM_PREFIX + user.getId(), fcmToken);
    }

    @Transactional
    public void signup(UserCreateDto userCreateDto) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(userCreateDto.getEmail()).orElseThrow(() -> new EmailNotVerifiedException("이메일 인증이 되지 않았습니다."));
        if (!emailVerification.getIsVerified()) {
            throw new EmailNotVerifiedException("이메일 인증이 되지 않았습니다.");
        }

        User user = User.builder()
                .email(userCreateDto.getEmail())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .nickname(userCreateDto.getNickname())
                .address(userCreateDto.getAddress())
                .latitude(userCreateDto.getLatitude())
                .longitude(userCreateDto.getLongitude())
                .build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void checkNickname(String nickname) {
        if(userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException("중복된 닉네임입니다.");
        }
    }

    @Transactional
    public void changePassword(User user, String password, String newPassword){
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
        User loginedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        loginedUser.changePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void changeUserInfo(User user, String nickname, String address, Double latitude, Double longitude) {
        User loginedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        loginedUser.changeUserInfo(nickname, address, latitude, longitude);
    }

    @Transactional
    public void changeUserImage(User user, String profileImage) {
        if (user.getProfileImage()!=null) {
            s3UploadService.delete(user.getProfileImage());
        }
        User loginedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));
        loginedUser.changeProfileImage(profileImage);
    }

    @Transactional
    public void successPayment(PointChargeDto pointDto, User user) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        UUID uuid = UUID.randomUUID();
        String mdk = uuid.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic dGVzdF9za19XZDQ2cW9wT0I4OW1CNlJtWnZhOFptTTc1eTB2Og==");
        headers.set("Content-Type", "application/json");
        headers.set("Idempotency-Key",mdk);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        Map<String, Object> param = new HashMap<>();
        param.put("paymentKey", pointDto.getPaymentKey());
        param.put("orderId", pointDto.getOrderId());
        param.put("amount", pointDto.getAmount());

        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(param), headers);

            ResponseEntity response = restTemplate.postForEntity(url, requestEntity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new PaymentException("결제 오류가 발생하었습니다.");
            }

            User loginedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
            loginedUser.changePoint(pointDto.getAmount()+loginedUser.getPoint());
            userRepository.save(loginedUser);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<PostListDto> getMyPostList(Integer page, User user){
        Pageable pageable = PageRequest.of(page-1, 15);
        return postRepository.findAllByUserIdOrderByIdDesc(user.getId(), pageable)
                .stream()
                .map(post -> new PostListDto(post))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<JointDeliveryListDto> getMyWrittenJointDeliveries(int size, int page, User user){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<JointDelivery> myWrittenJointDeliveryList = jointDeliveryRepository.findAllByUserId(user.getId(), pageable).toList();

        List<JointDeliveryListDto> myWrittenJointDeliveryListDtoList = new ArrayList<>();
        for(JointDelivery jointDelivery : myWrittenJointDeliveryList){
             JointDeliveryListDto jointDeliveryListDto = new JointDeliveryListDto(jointDelivery,-1);
            myWrittenJointDeliveryListDtoList.add(jointDeliveryListDto);
        }
        return myWrittenJointDeliveryListDtoList;
    }


    @Transactional(readOnly = true)
    public List<JointDeliveryListDto> getMyJoinedJointDeliveries(Integer size, Integer page, User user){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<JointDeliveryListDto> myJointDeliveryListDtoList = new ArrayList<>();
        List<JointDelivery> myJointDelivery = jointDeliveryCartRepository.findDistinctJointDeliveryByUserId(user.getId(), pageable);
        for(JointDelivery jointDelivery : myJointDelivery){ // 내가 참여한 jointdeliverycart 카트, 이 카트가 담긴 주문을 알아내야함
            JointDeliveryListDto jointDeliveryListDto = new JointDeliveryListDto(jointDelivery,-1);
            myJointDeliveryListDtoList.add(jointDeliveryListDto);
        }
        return myJointDeliveryListDtoList;
    }

    @Transactional
    public void withdrawPoint(Integer point, User user){
        User loginedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
        loginedUser.subtractPoint(point);
    }

    @Transactional
    public UserInfoDto getUserInfo(Long id){
        User loginedUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
        UserInfoDto userInfo = new UserInfoDto(loginedUser);
        System.out.println(userInfo.getEmail());
        return userInfo;
    }

    @Transactional
    public ResponseEntity<Void> deleteUser(User user){
        User loginedUser = userRepository.findById(user.getId()).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
        loginedUser.deleteUser();
        return ResponseEntity.ok().build();
    }


}
