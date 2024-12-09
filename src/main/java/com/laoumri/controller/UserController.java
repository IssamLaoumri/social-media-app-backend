package com.laoumri.controller;

import com.laoumri.common.AppConstants;
import com.laoumri.common.UserPrincipal;
import com.laoumri.dto.*;
import com.laoumri.entity.User;
import com.laoumri.response.PostResponse;
import com.laoumri.response.UserResponse;
import com.laoumri.service.JwtTokenService;
import com.laoumri.service.PostService;
import com.laoumri.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupDto signupDto) {
        User savedUser = userService.createNewUser(signupDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword())
        );
        User loginUser = userService.getUserByEmail(loginDto.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders newHttpHeaders = new HttpHeaders();
        newHttpHeaders.add(AppConstants.TOKEN_HEADER, jwtTokenService.generateToken(userPrincipal));
        return new ResponseEntity<>(loginUser, newHttpHeaders, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> showUserProfile(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getPrincipal().toString());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/account/update/info")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserInfoDto updateUserInfoDto) {
        User updatedUser = userService.updateUserInfo(updateUserInfoDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/account/update/email")
    public ResponseEntity<?> updateUserEmail(@RequestBody @Valid UpdateEmailDto updateEmailDto) {
        userService.updateEmail(updateEmailDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        userService.updatePassword(updatePasswordDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/update/profile-photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestParam MultipartFile profilePhoto) {
        User updatedUser = userService.updateProfilePhoto(profilePhoto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/account/update/cover-photo")
    public ResponseEntity<?> updateCoverPhoto(@RequestParam MultipartFile coverPhoto) {
        User updatedUser = userService.updateCoverPhoto(coverPhoto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/account/delete")
    public ResponseEntity<?> deleteUserAccount() {
        userService.deleteUserAccount();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable Long userId) {
        userService.followUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long userId) {
        userService.unfollowUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/following")
    public ResponseEntity<?> getUserFollowingUsers(@PathVariable Long userId,
                                                  @RequestParam Integer page,
                                                  @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UserResponse> followingList = userService.getFollowingUsersPaginate(userId, page, size);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/follower")
    public ResponseEntity<?> getUserFollowerUsers(@PathVariable Long userId,
                                                 @RequestParam Integer page,
                                                 @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UserResponse> followingList = userService.getFollowerUsersPaginate(userId, page, size);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        User authUser = userService.getAuthenticatedUser();
        User targetUser = userService.getUserById(userId);
        UserResponse userResponse = UserResponse.builder()
                .user(targetUser)
                .followedByAuthUser(targetUser.getFollowerUsers().contains(authUser))
                .build();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable Long userId,
                                          @RequestParam Integer page,
                                          @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        User targetUser = userService.getUserById(userId);
        List<PostResponse> userPosts = postService.getPostsByUserPaginate(targetUser, page, size);
        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUser(@RequestParam String key,
                                        @RequestParam Integer page,
                                        @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UserResponse> userSearchResult = userService.getUserSearchResult(key, page, size);
        return new ResponseEntity<>(userSearchResult, HttpStatus.OK);
    }

    @PostMapping("/verify-email/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable String token) {
        userService.verifyEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto,
                                           @PathVariable String token) {
        userService.resetPassword(token, resetPasswordDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
