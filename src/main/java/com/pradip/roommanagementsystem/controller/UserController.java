package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.RegisterUser;
import com.pradip.roommanagementsystem.dto.UserDTO;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.service.UserService;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> getAlUsers(@RequestParam("projection") String projectionName) throws ClassNotFoundException {
        return ResponseEntity.ok(userService.getAllUsers(projectionName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(
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
}