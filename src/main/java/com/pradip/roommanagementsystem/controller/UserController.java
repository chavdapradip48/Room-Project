package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.ChangePasswordDTO;
import com.pradip.roommanagementsystem.dto.RegisterUser;
import com.pradip.roommanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<List<?>>> getAlUsers(@RequestParam(required = false,value = "projection") String projectionName) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                        "Users fetched successfully.",userService.getAllUsers(projectionName))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getUserById(
            @RequestParam(required = false,value = "projection")  String projectionName,
            @PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "User fetched successfully.",userService.getUserById(id, projectionName))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterUser>> createUser(@Valid @RequestBody RegisterUser user){
        return ResponseEntity.ok(new ApiResponse<RegisterUser>(HttpStatus.OK.value(),
                "User saved successfully.",userService.createUser(user))
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<RegisterUser>> updateUser(@Valid @RequestBody RegisterUser user){
        return ResponseEntity.ok(new ApiResponse<RegisterUser>(HttpStatus.OK.value(),
                "User updated successfully.",userService.createUser(user))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<Object>(HttpStatus.OK.value(),
                "User deleted successfully.",userService.deleteUserById(id))
        );
    }

    @GetMapping("/send-otp/{email}")
    public ResponseEntity<ApiResponse<String>> sendEmail(@PathVariable String email)  {
        return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.OK.value(),
                userService.sendOtpToEmail(email))
        );
    }

    @GetMapping("/verify-otp/{email}/{otp}")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@PathVariable String email,@PathVariable String otp) throws ClassNotFoundException, MessagingException {
        return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.OK.value(),
                userService.verifyOtpToEmail(email,otp))
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody @Valid ChangePasswordDTO password){
        return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.OK.value(), "Password updated successfully",userService.changePassword(password)));
    }
}