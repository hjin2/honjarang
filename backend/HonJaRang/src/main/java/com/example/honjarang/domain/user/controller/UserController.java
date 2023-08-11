package com.example.honjarang.domain.user.controller;

import com.example.honjarang.domain.jointdelivery.dto.JointDeliveryListDto;
import com.example.honjarang.domain.post.dto.PostListDto;
import com.example.honjarang.domain.secondhand.dto.TransactionListDto;
import com.example.honjarang.domain.user.dto.*;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.service.EmailService;
import com.example.honjarang.domain.user.service.UserService;
import com.example.honjarang.security.CurrentUser;
import com.example.honjarang.security.dto.TokenDto;
import com.example.honjarang.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final TokenService tokenService;

    private final EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        User user = userService.login(loginDto);
        TokenDto tokenDto = tokenService.generateToken(user.getId(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(@RequestBody Map<String, Object> map, @CurrentUser User user) {
        userService.addFcmToken(user, (String) map.get("fcm_token"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, Object> map, @CurrentUser User user) {
        userService.deleteFcmToken(user, (String) map.get("fcm_token"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody Map<String, Object> map, @CurrentUser User user) {
        tokenService.verifyToken((String) map.get("refresh_token"));
        TokenDto tokenDto = tokenService.generateToken(user.getId(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Void> checkEmail(@RequestParam String email) {
        userService.checkEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody Map<String, String> body) {
        emailService.sendVerificationCode(body.get("email"));
//        emailService.sendVerificationCodeForTest(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeDto verifyCodeDto) {
        emailService.verifyCode(verifyCodeDto.getEmail(), verifyCodeDto.getCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Void> checkNickname(@RequestParam String nickname) {
        userService.checkNickname(nickname);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserCreateDto userCreateDto) {
        userService.signup(userCreateDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody PasswordUpdateDto passwordUpdateDto, @CurrentUser User user) {
        userService.changePassword(passwordUpdateDto.getPassword(), passwordUpdateDto.getNewPassword(), user);
    }

    @PutMapping("/users")
    public ResponseEntity<Void> changeUserInfo(@RequestBody UserInfoUpdateDto userInfoUpdateDto, @CurrentUser User user) {
        userService.changeUserInfo(user, userInfoUpdateDto.getNickname(), userInfoUpdateDto.getAddress(), userInfoUpdateDto.getLatitude(), userInfoUpdateDto.getLongitude());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/success")
    public ResponseEntity<Void> successPayment(@RequestBody PointChargeDto pointDto, @CurrentUser User user){
        userService.successPayment(pointDto, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostListDto>> getMyPostList(@RequestParam(value = "size", defaultValue = "1") Integer size, @RequestParam(value="page", defaultValue = "1") int page, @CurrentUser User user){
        List<PostListDto> postList = userService.getMyPostList(page,size, user);
        return ResponseEntity.ok(postList);
    }


    @GetMapping("/joint-deliveries-writer")
    public ResponseEntity<List<JointDeliveryListDto>> getMyWrittenJointDeliveries(@RequestParam(value = "size", defaultValue = "1") int size, @RequestParam(value = "page", defaultValue = "1") int page, @CurrentUser User user){
        
        List<JointDeliveryListDto> myWrittenJointDeliveryListDtoList = userService.getMyWrittenJointDeliveries(page,size,user);
        return ResponseEntity.ok(myWrittenJointDeliveryListDtoList);
    }

    @GetMapping("/joint-deliveries-participating")
    public ResponseEntity<List<JointDeliveryListDto>> getMyJoinedJointDeliveries(@RequestParam(value = "size", defaultValue = "1") int size, @RequestParam(value = "page", defaultValue = "1") int page, @CurrentUser User user){
        List<JointDeliveryListDto> myJoinedJointDeliveryListDtoList = userService.getMyJoinedJointDeliveries(page,size,user);
        return ResponseEntity.ok(myJoinedJointDeliveryListDtoList);
    }

    @GetMapping("/transaction-writer")
    public ResponseEntity<List<TransactionListDto>> getMyTransactions(@RequestParam(value = "size", defaultValue = "1") int size, @RequestParam(value = "page", defaultValue = "1") int page, @CurrentUser User user){
        List<TransactionListDto> transactionListDtoList = userService.getMyTransactions(page,size,user);
        return ResponseEntity.ok(transactionListDtoList);
    }

    @GetMapping("/transaction-participating")
    public ResponseEntity<List<TransactionListDto>> getMyJoinedTransactions(@RequestParam(value = "size", defaultValue = "1") int size, @RequestParam(value = "page", defaultValue = "1") int page, @CurrentUser User user){
        List<TransactionListDto> transactionListDtoList = userService.getMyJoinedTransactions(page,size,user);
        return ResponseEntity.ok(transactionListDtoList);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Void> withdrawPoint(@RequestBody PointWithdrawDto pointWithdrawDto, @CurrentUser User user){
        userService.withdrawPoint(pointWithdrawDto.getPoint(),user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoDto> getUserInfo(@RequestParam Long id){
        UserInfoDto userInfo = userService.getUserInfo(id);
        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile-image")
    public ResponseEntity<Void> uploadProfileImage(@RequestParam("profile_image") MultipartFile profileImage, @CurrentUser User user){
        userService.updateProfileImage(profileImage, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page-post")
    public ResponseEntity<Integer> getMyPostsPageCount(@RequestParam Integer size, @CurrentUser User user) {
        return ResponseEntity.ok(userService.getMyPostsPageCount(size, user));
    }

    @GetMapping("/page-writing")
    public ResponseEntity<Integer> getMyWrittenJointDeliveriesPageCount(@RequestParam Integer size, @CurrentUser User user) {
        return ResponseEntity.ok(userService.getMyWrittenJointDeliveriesPageCount(size, user));
    }

    @GetMapping("/page-join")
    public ResponseEntity<Integer> getMyJoinedJointDeliveriesPageCount(@RequestParam Integer size, @CurrentUser User user) {
        return ResponseEntity.ok(userService.getMyJoinedJointDeliveriesPageCount(size, user));
    }

    @GetMapping("/page-transaction")
    public ResponseEntity<Integer> getMyTransactionPageCount(@RequestParam Integer size, @CurrentUser User user) {
        return ResponseEntity.ok(userService.getMyTransactionPageCount(size, user));
    }

    @GetMapping("/page-jointransaction")
    public ResponseEntity<Integer> getMyJoinedTransactionPageCount(@RequestParam Integer size, @CurrentUser User user) {
        return ResponseEntity.ok(userService.getMyJoinedTransactionPageCount(size, user));
    }




}
