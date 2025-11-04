package com.xiaofei.my_tech_blog.controller;

import com.xiaofei.my_tech_blog.entity.User;
import com.xiaofei.my_tech_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // 这个注解告诉Spring这是一个Controller，并且返回值直接作为HTTP响应体
@RequestMapping("/api/users") // 为这个Controller的所有端点设置基础路径
public class UserController {
    @Autowired
    private UserService userService;

    // 用户注册API
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(
            @RequestBody Map<String, String> request) { // @RequestBody将JSON自动转为Map

        Map<String, Object> response = new HashMap<>();

        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");

            // 基本验证
            if (username == null || username.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "用户名不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (password == null || password.length() < 6) {
                response.put("success", false);
                response.put("message", "密码长度至少6位");
                return ResponseEntity.badRequest().body(response);
            }

            // 调用Service层注册用户
            User user = userService.registerUser(username, email, password);

            response.put("success", true);
            response.put("message", "注册成功");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 用户登录API
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(
            @RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        try {
            String username = request.get("username");
            String password = request.get("password");

            User user = userService.loginUser(username, password);

            response.put("success", true);
            response.put("message", "登录成功");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 检查用户名是否可用
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsernameAvailability(
            @RequestParam String username) {

        Map<String, Object> response = new HashMap<>();
        boolean available = userService.isUsernameAvailable(username);

        response.put("available", available);
        response.put("message", available ? "用户名可用" : "用户名已存在");

        return ResponseEntity.ok(response);
    }

    // 获取用户信息
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long userId) {

        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.getUserById(userId);

            response.put("success", true);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "createdAt", user.getCreatedAt()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}