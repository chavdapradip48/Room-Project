package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.ChangePasswordDTO;
import com.pradip.roommanagementsystem.dto.RegisterUser;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void createDefaultUser() {
        userService.createDefaultUser();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> getAlUsers(@RequestParam("projection") String projectionName) throws ClassNotFoundException {
        return ResponseEntity.ok(userService.getAllUsers(projectionName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getUserById(
            @RequestParam("projection") String projectionName,
            @PathVariable Long id) throws ClassNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id, projectionName));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterUser>> createUser(@RequestBody RegisterUser user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<RegisterUser>> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) throws ClassNotFoundException {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @GetMapping("/send-otp/{email}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ApiResponse<String>> sendEmail(@PathVariable String email) throws ClassNotFoundException, MessagingException {
        return ResponseEntity.ok(userService.sendOtpToEmail(email));
    }

    @GetMapping("/verify-otp/{email}/{otp}")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@PathVariable String email,@PathVariable String otp) throws ClassNotFoundException, MessagingException {
        return ResponseEntity.ok(userService.verifyOtpToEmail(email,otp));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody @Valid ChangePasswordDTO password){
        return ResponseEntity.ok(userService.changePassword(password));
    }
}
