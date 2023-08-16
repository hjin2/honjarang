package com.example.honjarang.domain.user.service;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryListDto;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryApplicant;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.jointdelivery.exception.MenuNotFoundException;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryApplicantRepository;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryCartRepository;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryRepository;
import com.example.honjarang.domain.jointdelivery.repository.MenuRepository;
import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseDto;
import com.example.honjarang.domain.jointpurchase.dto.JointPurchaseListDto;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchase;
import com.example.honjarang.domain.jointpurchase.entity.JointPurchaseApplicant;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseApplicantRepository;
import com.example.honjarang.domain.jointpurchase.repository.JointPurchaseRepository;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.exception.PaymentException;
import com.example.honjarang.domain.post.repository.CommentRepository;
import com.example.honjarang.domain.post.repository.LikePostRepository;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.secondhand.entity.Transaction;
import com.example.honjarang.domain.secondhand.repository.TransactionRepository;
import com.example.honjarang.domain.user.dto.*;
import com.example.honjarang.domain.user.entity.EmailVerification;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.*;
import com.example.honjarang.domain.user.repository.EmailVerificationRepository;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.example.honjarang.security.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final S3Client s3Client;

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

    private final JointDeliveryApplicantRepository jointDeliveryApplicantRepository;

    private final TransactionRepository transactionRepository;

    private final JointPurchaseRepository jointPurchaseRepository;

    private final JointPurchaseApplicantRepository jointPurchaseApplicantRepository;


    private final LikePostRepository likePostRepository;
    private final CommentRepository commentRepository;



    private final MenuRepository menuRepository;

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
                .profileImage("https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/basic.jpg")
                .build();
        userRepository.save(user);
        emailVerificationRepository.delete(emailVerification);
    }

    @Transactional(readOnly = true)
    public void checkNickname(String nickname) {
        if(userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException("중복된 닉네임입니다.");
        }
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다.");
        }
    }

    @Transactional(readOnly = true)
    public void setNewPassword(PasswordSetDto passwordSetDto)
    {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(passwordSetDto.getEmail()).orElseThrow(() -> new EmailNotVerifiedException("이메일 인증이 되지 않았습니다."));
        if(!emailVerification.getIsVerified()){
            throw new EmailNotVerifiedException("이메일 인증이 되지 않았습니다.");
        }

        User user = userRepository.findByEmail(passwordSetDto.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        user.changePassword(passwordEncoder.encode(passwordSetDto.getNewPassword()));
        emailVerificationRepository.delete(emailVerification);
    }

    @Transactional
    public void changePassword(String password, String newPassword, @CurrentUser User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
        User loginedUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        loginedUser.changePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void changeUserInfo(User user, String nickname, String address, Double latitude, Double longitude) {
        User loginedUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        loginedUser.changeUserInfo(nickname, address, latitude, longitude);
    }

    @Transactional
    public void updateProfileImage(MultipartFile profileImage, User loginUser) {
        // 기존 프로필 이미지 삭제
        if (loginUser.getProfileImage()!=null) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("profileImage/" + loginUser.getProfileImage())
                    .build());
        }

        try {
           s3Client.putObject(PutObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("profileImage/" + profileImage.getOriginalFilename())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(profileImage.getContentType())
                    .build(), RequestBody.fromInputStream(profileImage.getInputStream(), profileImage.getSize()));
            User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
            user.changeProfileImage("https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/"+profileImage.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 업로드에 실패했습니다.");
        }
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

            User loginedUser = userRepository.findById(user.getId()).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
            loginedUser.changePoint(pointDto.getAmount()+loginedUser.getPoint());
            userRepository.save(loginedUser);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void successPaymentForTest(PointChargeDto pointDto, User user) {
        User loginedUser = userRepository.findById(user.getId()).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
        loginedUser.addPoint(pointDto.getAmount());
    }

    @Transactional(readOnly = true)
    public List<PostListDto> getMyPostList(Integer page, Integer size, Long userId){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<Post> posts = postRepository.findAllByUserIdOrderByIdDesc(userId, pageable).toList();

        List<PostListDto> postListDtos = new ArrayList<>();
        for(Post post : posts){
            Integer likeCnt = likePostRepository.countByPostId(post.getId());
            Integer commentCnt = commentRepository.countByPostId(post.getId());
            postListDtos.add(new PostListDto(post, likeCnt, commentCnt));
        }

        return postListDtos;
    }

    @Transactional(readOnly = true)
    public List<JointDeliveryListDto> getMyWrittenJointDeliveries(int page, int size, Long userId){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<JointDelivery> myWrittenJointDeliveryList = jointDeliveryRepository.findAllByUserId(userId, pageable).toList();
        List<JointDeliveryListDto> myWrittenJointDeliveryListDtoList = new ArrayList<>();
        for(JointDelivery jointDelivery : myWrittenJointDeliveryList){
            // 총 가격 계산
            int currentToalPrice = 0;
            List<JointDeliveryCart> jointDeliveryCartList = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDelivery.getId());
            for(JointDeliveryCart jointDeliveryCart : jointDeliveryCartList){
                Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId())).orElseThrow(()->new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
                currentToalPrice += menu.getPrice() * jointDeliveryCart.getQuantity();
            }
             JointDeliveryListDto jointDeliveryListDto = new JointDeliveryListDto(jointDelivery,currentToalPrice);
            myWrittenJointDeliveryListDtoList.add(jointDeliveryListDto);
        }
        return myWrittenJointDeliveryListDtoList;
    }


    @Transactional(readOnly = true)
    public List<JointDeliveryListDto> getMyJoinedJointDeliveries(Integer page, Integer size, User user){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<JointDeliveryListDto> myJointDeliveryListDtoList = new ArrayList<>();
        List<JointDelivery> myJointDelivery = jointDeliveryCartRepository.findDistinctJointDeliveryByUserId(user.getId(), pageable);
        for(JointDelivery jointDelivery : myJointDelivery){ // 내가 참여한 jointdeliverycart 카트, 이 카트가 담긴 주문을 알아내야함
                int currentToalPrice = 0;
                List<JointDeliveryCart> jointDeliveryCartList = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDelivery.getId());
                for(JointDeliveryCart jointDeliveryCart : jointDeliveryCartList) {
                    Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId())).orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
                    currentToalPrice += menu.getPrice() * jointDeliveryCart.getQuantity();
                }
            JointDeliveryListDto jointDeliveryListDto = new JointDeliveryListDto(jointDelivery,currentToalPrice);
            myJointDeliveryListDtoList.add(jointDeliveryListDto);
        }
        return myJointDeliveryListDtoList;
    }

    @Transactional(readOnly = true)
    public List<TransactionListDto> getMyTransactions(Integer page, Integer size, Long userId){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<TransactionListDto> myTransactionListDtoList = new ArrayList<>();
        List<Transaction> myTransaction = transactionRepository.findAllBySellerId(userId,pageable).toList();
        for(Transaction transaction : myTransaction){
            TransactionListDto myTransactionListDto = new TransactionListDto(transaction);
            myTransactionListDtoList.add(myTransactionListDto);
        }
        return myTransactionListDtoList;
    }

    @Transactional(readOnly = true)
    public List<TransactionListDto> getMyJoinedTransactions(Integer page, Integer size, User user){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<TransactionListDto> myTransactionListDtoList = new ArrayList<>();
        List<Transaction> myTransaction = transactionRepository.findAllByBuyerId(user.getId(),pageable).toList();
        for(Transaction transaction : myTransaction){
            TransactionListDto myTransactionListDto = new TransactionListDto(transaction);
            myTransactionListDtoList.add(myTransactionListDto);
        }
        return myTransactionListDtoList;
    }

    @Transactional(readOnly = true)
    public List<JointPurchaseListDto> getMyJointPurchase(Integer page, Integer size, Long userId){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<JointPurchaseListDto> myJointPurchaseListDtos = new ArrayList<>();
        List<JointPurchase> myJointPurchaseList = jointPurchaseRepository.findAllByUserId(userId,pageable).toList();
        for(JointPurchase jointPurchase : myJointPurchaseList){
            Integer currentPersonCnt = jointPurchaseApplicantRepository.countByJointPurchaseId(jointPurchase.getId());
            JointPurchaseListDto myJointPurchaseListDto = new JointPurchaseListDto(jointPurchase,currentPersonCnt);
            myJointPurchaseListDtos.add(myJointPurchaseListDto);
        }
        return myJointPurchaseListDtos;
    }

    @Transactional(readOnly = true)
    public List<JointPurchaseListDto> getMyJoinedJointPurchase(Integer page, Integer size, User user){
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);
        List<JointPurchaseListDto> myJointPurchaseListDtos = new ArrayList<>();
        List<Long> jointPurchaseIds = new ArrayList<>();

        List<JointPurchaseApplicant> myJointPurchaseList = jointPurchaseApplicantRepository.findAllByUserId(user.getId(),pageable).toList();
        for(JointPurchaseApplicant jointPurchase : myJointPurchaseList){
            jointPurchaseIds.add(jointPurchase.getJointPurchase().getId());
        }

        // jointPurchaseIds여기에 이제 공동구매목록 번호들이 들어있음
        // 이 번호에 해당하는 공동구미listdto를 내보내야함
        List<JointPurchaseListDto> results = new ArrayList<>();
        for(Long jointId : jointPurchaseIds) {
            JointPurchase tmp = jointPurchaseRepository.findAllById(jointId);
            Integer currentPersonCnt = jointPurchaseApplicantRepository.countByJointPurchaseId(jointId);
            JointPurchaseListDto tmpDto = new JointPurchaseListDto(tmp, currentPersonCnt);
            results.add(tmpDto);
        }
        return results;
    }

    @Transactional
    public void withdrawPoint(Integer point, User user){
        User loginedUser = userRepository.findById(user.getId()).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
        if(user.getPoint() < point){
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        loginedUser.subtractPoint(point);
    }

    @Transactional
    public UserInfoDto getUserInfo(Long id){
        User loginedUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
        if(loginedUser.getIsDeleted()){
            throw new UserNotFoundException("탈퇴한 사용자 입니다.");
        }
        UserInfoDto userInfo = new UserInfoDto(loginedUser);
        return userInfo;
    }

    @Transactional
    public ResponseEntity<Void> deleteUser(Long userId){
        User loginedUser = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다."));
        loginedUser.deleteUser();
        return ResponseEntity.ok().build();
    }

    @Transactional(readOnly = true)
    public Integer getMyPostsPageCount(Integer size, Long userId) {
        return (int) Math.ceil((double) postRepository.countAllByUserId(userId) / size) ;
    }



    @Transactional(readOnly = true)
    public Integer getMyWrittenJointDeliveriesPageCount(Integer size, Long userId) {
        return (int) Math.ceil((double) jointDeliveryRepository.countAllByUserId(userId) / size) ;
    }

    @Transactional(readOnly = true)
    public Integer getMyJoinedJointDeliveriesPageCount(Integer size, User user) {
        return (int) Math.ceil((double) jointDeliveryApplicantRepository.countAllByUserId(user.getId()) / size) ;
    }

    @Transactional(readOnly = true)
    public Integer getMyTransactionPageCount(Integer size, Long userId) {
        return (int) Math.ceil((double) transactionRepository.countAllBySellerId(userId) / size) ;
    }

    @Transactional(readOnly = true)
    public Integer getMyJoinedTransactionPageCount(Integer size, User user) {
        return (int) Math.ceil((double) transactionRepository.countAllByBuyerId(user.getId()) / size) ;
    }

    @Transactional(readOnly = true)
    public Integer getMyJointPurchasePageCount(Integer size, Long userId) {
        return (int) Math.ceil((double) jointPurchaseRepository.countAllByUserId(userId) / size) ;
    }

    @Transactional(readOnly = true)
    public Integer getMyJoinedJointPurchasePageCount(Integer size, User user) {
        return (int) Math.ceil((double) jointPurchaseApplicantRepository.countAllByUserId(user.getId()) / size) ;
    }
}
