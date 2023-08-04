package com.example.honjarang.domain.user.service;

import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryListDto;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryCartRepository;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryRepository;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.post.service.PostService;
import com.example.honjarang.domain.user.dto.LoginDto;
import com.example.honjarang.domain.user.dto.UserCreateDto;
import com.example.honjarang.domain.user.entity.EmailVerification;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.DuplicateNicknameException;
import com.example.honjarang.domain.user.exception.EmailNotVerifiedException;
import com.example.honjarang.domain.user.exception.PasswordMismatchException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.EmailVerificationRepository;
import com.example.honjarang.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private JointDeliveryRepository jointDeliveryRepository;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private JointDeliveryCartRepository jointDeliveryCartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private EmailVerification emailVerification;

    private JointDelivery jointDelivery;
    private Store store;
    @Spy
    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@test.com")
                .password("test1234")
                .nickname("테스트")
                .point(10000)
                .address("서울특별시 강남구")
                .latitude(37.123456)
                .longitude(127.123456)
                .role(Role.ROLE_USER)
                .build();
        user.setIdForTest(1L);
        emailVerification = EmailVerification.builder()
                .email("test@test.com")
                .code("123456")
                .isVerified(true)
                .build();
        emailVerification.setIdForTest(1L);


        ///
        post = Post.builder()
                .title("test")
                .user(user)
                .views(0)
                .category(Category.FREE)
                .isNotice(false)
                .content("test")
                .build();
        post.setCreatedAtForTest(DateTimeUtils.parseLocalDateTime("2023-08-02 12:00:11"));

        store = Store.builder()
                .id(1L)
                .storeName("테스트 가게명")
                .image("test_store.jpg")
                .address("서울특별시 강남구")
                .latitude(25.444)
                .longitude(34.334)
                .build();
        store.setIdForTest(1L);


        jointDelivery = JointDelivery.builder()
                .store(store)
                .user(user)
                .content("테스트 매장 내용입니다.")
                .deliveryCharge(3000)
                .targetMinPrice(20000)
                .deadline(DateTimeUtils.parseLocalDateTime("2023-08-02 12:20:00"))
                .build();
        jointDelivery.setIdForTest(1L);
        jointDelivery.setDeadlineForTest(DateTimeUtils.parseLocalDateTime("2023-08-02 12:00:00"));
        jointDelivery.setUserForTest(user);
        jointDelivery.setCanceledForTest(false);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));

    }


    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");

        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("test1234", "test1234")).willReturn(true);

        // when
        User user = userService.login(loginDto);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getPassword()).isEqualTo("test1234");
        assertThat(user.getNickname()).isEqualTo("테스트");
        assertThat(user.getAddress()).isEqualTo("서울특별시 강남구");
        assertThat(user.getLatitude()).isEqualTo(37.123456);
        assertThat(user.getLongitude()).isEqualTo(127.123456);
        assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @DisplayName("로그인 실패 - 사용자가 존재하지 않는 경우")
    public void login_UserNotFoundException() {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.login(loginDto));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 일치하지 않는 경우")
    public void login_PasswordMismatchException() {
        // given
        LoginDto loginDto = new LoginDto("test@test.com", "test1234");
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("test1234", "test1234")).willReturn(false);

        // when & then
        assertThrows(PasswordMismatchException.class, () -> userService.login(loginDto));
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // given
        UserCreateDto userCreateDto = new UserCreateDto("test@test.com", "test1234", "테스트", "서울특별시 강남구", 37.123456, 127.123456);

        given(emailVerificationRepository.findByEmail("test@test.com")).willReturn(Optional.of(emailVerification));

        // when
        userService.signup(userCreateDto);

        // then
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 인증이 되지 않은 경우")
    void signup_EmailNotVerifiedException() {
        // given
        emailVerification.setIsVerifiedForTest(false);
        UserCreateDto userCreateDto = new UserCreateDto("test@test.com", "test1234", "테스트", "서울특별시 강남구", 37.123456, 127.123456);

        given(emailVerificationRepository.findByEmail("test@test.com")).willReturn(Optional.of(emailVerification));

        // when & then
        assertThrows(EmailNotVerifiedException.class, () -> userService.signup(userCreateDto));
    }

    @Test
    @DisplayName("닉네임 중복 체크 성공")
    void checkNickname_Success() {
        // given
        given(userRepository.existsByNickname("테스트")).willReturn(false);

        // when
        userService.checkNickname("테스트");

        // then
    }

    @Test
    @DisplayName("닉네임 중복 체크 실패 - 중복된 닉네임인 경우")
    void checkNickname_DuplicateNicknameException() {
        // given
        given(userRepository.existsByNickname("테스트")).willReturn(true);

        // when & then
        assertThrows(DuplicateNicknameException.class, () -> userService.checkNickname("테스트"));
    }


    @Test
    @DisplayName("비밀번호 변경 성공")
    public void changePassword_Success() {
        // given
        User expectedUser = User.builder()
                .email("test@test.com")
                .password("test1234")
                .build();

        given(passwordEncoder.matches("test1234", expectedUser.getPassword())).willReturn(true);
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(expectedUser));
        // When
        userService.changePassword(expectedUser, "test1234", "new1234");

        assertThat(expectedUser.getPassword()).isEqualTo(passwordEncoder.encode("new1234"));
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 사용자가 입력한 현재 비밀번호가 일치하지 않는 경우")
    public void changePassword_PasswordMismatchException() {
        // given
        User expectedUser = User.builder()
                .email("test@test.com")
                .password("test1234")
                .build();

        given(passwordEncoder.matches("test1234", expectedUser.getPassword())).willReturn(false);

        // When
        assertThrows(PasswordMismatchException.class, () -> userService.changePassword(expectedUser, "test1234", "new1234"));
    }



    @Test
    @DisplayName("회원정보 수정 성공")
    void changeUserInfo_Success() {
        // when
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        userService.changeUserInfo(user, "수정된 테스트 닉네임","경상북도 구미시", 10.23445, 23.32423);

        // then
        assertThat(user.getNickname()).isEqualTo("수정된 테스트 닉네임");
        assertThat(user.getAddress()).isEqualTo("경상북도 구미시");
        assertThat(user.getLatitude()).isEqualTo(10.23445);
        assertThat(user.getLongitude()).isEqualTo(23.32423);
    }


    @Test
    @DisplayName("회원정보 수정 실패 - 사용자가 존재하지 않는 경우")
    void changeUserInfo_UserNotFoundException() {
        // given
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.changeUserInfo(user,"수정된 테스트 닉네임","경상북도 구미시",10.23445,23.32423));
    }

    @Test
    @DisplayName("회원정보 이미지 변경 성공 - 기존에 이미지가 없을 경우")
    void changeImage_Success() {
        // given
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // when
        userService.changeUserImage(user,"test.jpg");

        // then
        assertThat(user.getProfileImage()).isEqualTo("test.jpg");

    }

    @Test
    @DisplayName("회원정보 이미지 변경 실패 - 사용자가 존재하지 않는 경우")
    void changeImage_UserNotFoundException() {
        // given
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.changeUserImage(user,"test.jpg"));
    }


    @Test
    @DisplayName("내가 작성한 게시글 보기")
    void getMyPostList_Success(){
        // given
        Pageable pageable = PageRequest.of(0,15);


        List<Post> postList = List.of(post);
        given(postRepository.findAllByUserIdOrderByIdDesc(user.getId(),pageable)).willReturn(postList);

        PostListDto postListDto = new PostListDto(post);

        // when
        List<PostListDto> result = userService.getMyPostList(1,user);

        // then
        assertThat(result.get(0).getUserId()).isEqualTo(user.getId());
        assertThat(result.get(0).getCategory()).isEqualTo(post.getCategory());
        assertThat(result.get(0).getTitle()).isEqualTo(post.getTitle());
        assertThat(result.get(0).getContent()).isEqualTo(post.getContent());
        assertThat(result.get(0).getIsNotice()).isEqualTo(post.getIsNotice());
        assertThat(result.get(0).getViews()).isEqualTo(post.getViews());


    }


    @Test
    @DisplayName("내가 작성한 공동배달 글 보기")
    void getMyWrittenJointDeliveries_success() {
        // given
        Pageable pageable = Pageable.ofSize(15).withPage(0);
        List<JointDelivery> jointDeliveries = List.of(jointDelivery);
        given(jointDeliveryRepository.findAllByUserId(user.getId(), pageable)).willReturn(new PageImpl<>(jointDeliveries));

        JointDeliveryListDto jointDeliveryListDto = new JointDeliveryListDto(jointDelivery);
        List<JointDeliveryListDto> expectedResult = List.of(jointDeliveryListDto);


        // when
        List<JointDeliveryListDto> result = userService.getMyWrittenJointDeliveries(15, 1, user);


        // then : jointDelivery값이 JointDeliveryListDto에 담긴다 이걸 비교해야됨 즉 expectedResult
        assertThat(result.get(0).getUserId()).isEqualTo(expectedResult.get(0).getId());
        assertThat(result.get(0).getTargetMinPrice()).isEqualTo(expectedResult.get(0).getTargetMinPrice());
        assertThat(result.get(0).getStoreId()).isEqualTo(expectedResult.get(0).getStoreId());
        assertThat(result.get(0).getStoreName()).isEqualTo(expectedResult.get(0).getStoreName());
        assertThat(result.get(0).getStoreImage()).isEqualTo(expectedResult.get(0).getStoreImage());
        assertThat(result.get(0).getUserId()).isEqualTo(expectedResult.get(0).getUserId());
        assertThat(result.get(0).getNickname()).isEqualTo(expectedResult.get(0).getNickname());

    }


    @Test
    @DisplayName("내가 참여하는 공동배달 글 보기")
    void getMyJoinedJointDeliveries_success() {
        // given
        Pageable pageable = Pageable.ofSize(15).withPage(0);
        List<JointDelivery> jointDeliveries = List.of(jointDelivery);
        given(jointDeliveryCartRepository.findDistinctJointDeliveryByUserId(user.getId(), pageable)).willReturn(jointDeliveries);

        JointDeliveryListDto jointDeliveryListDto = new JointDeliveryListDto(jointDelivery);
        List<JointDeliveryListDto> expectedResult = List.of(jointDeliveryListDto);


        // when
        List<JointDeliveryListDto> result = userService.getMyJoinedJointDeliveries(15, 1, user);


        // then
        assertThat(result.get(0).getUserId()).isEqualTo(expectedResult.get(0).getId());
        assertThat(result.get(0).getTargetMinPrice()).isEqualTo(expectedResult.get(0).getTargetMinPrice());
        assertThat(result.get(0).getStoreId()).isEqualTo(expectedResult.get(0).getStoreId());
        assertThat(result.get(0).getStoreName()).isEqualTo(expectedResult.get(0).getStoreName());
        assertThat(result.get(0).getStoreImage()).isEqualTo(expectedResult.get(0).getStoreImage());
        assertThat(result.get(0).getUserId()).isEqualTo(expectedResult.get(0).getUserId());
        assertThat(result.get(0).getNickname()).isEqualTo(expectedResult.get(0).getNickname());

    }


    @Test
    @DisplayName("포인트 출금 기능")
    void withdrawPoint_success(){
        // given
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.ofNullable(user));

        // when
        userService.withdrawPoint(3000,user);

        // then
        assertThat(user.getPoint()).isEqualTo(7000);
    }
}