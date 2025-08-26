package com.connecthub.socialmedia.controller;

import com.connecthub.socialmedia.dto.ApiResponse;
import com.connecthub.socialmedia.dto.UserDto;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@Valid @RequestBody UserDto.SignUpRequest signUpRequest) {
    try {
      User user = authService.signUp(signUpRequest);

      UserDto.Response userResponse = new UserDto.Response(user);
      return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", userResponse));

    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signIn(@Valid @RequestBody UserDto.LoginRequest loginRequest) {
    try {
      String jwt = authService.signIn(loginRequest);

      Map<String, String> response = new HashMap<>();
      response.put("accessToken", jwt);
      response.put("tokenType", "Bearer");

      return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", response));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error("아이디 또는 비밀번호가 올바르지 않습니다."));
    }
  }

  @GetMapping("/check-username")
  public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
    boolean isAvailable = authService.isUsernameAvailable(username);

    Map<String, Boolean> response = new HashMap<>();
    response.put("available", isAvailable);

    String message = isAvailable ? "사용 가능한 사용자명입니다." : "이미 사용 중인 사용자명입니다.";
    return ResponseEntity.ok(ApiResponse.success(message, response));
  }

  @GetMapping("/check-email")
  public ResponseEntity<?> checkEmailAvailability(@RequestParam String email) {
    boolean isAvailable = authService.isEmailAvailable(email);

    Map<String, Boolean> response = new HashMap<>();
    response.put("available", isAvailable);

    String message = isAvailable ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.";
    return ResponseEntity.ok(ApiResponse.success(message, response));
  }
}
