package com.pradip.roommanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pradip.roommanagementsystem.dto.*;
import com.pradip.roommanagementsystem.dto.projection.ChangePasswordUser;
import com.pradip.roommanagementsystem.dto.projection.UserDTO;
import com.pradip.roommanagementsystem.entity.Address;
import com.pradip.roommanagementsystem.entity.Otp;
import com.pradip.roommanagementsystem.entity.Role;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.exception.EmailException;
import com.pradip.roommanagementsystem.exception.UnauthorizedException;
import com.pradip.roommanagementsystem.exception.UserAlreadyExistlException;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.security.dto.CustomUserDetails;
import com.pradip.roommanagementsystem.security.dto.LoginRequest;
import com.pradip.roommanagementsystem.security.util.JwtUtils;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.awt.datatransfer.Clipboard;
import java.util.*;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private GeneralUtil util;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    private static final String projectionPackage = "com.pradip.roommanagementsystem.dto.projection.";

    public List<?> getAllUsers(String projectionName) {
//        List<User> allBy = userRepository.findAll();
        System.out.println(projectionName);
                List<?> allBy = userRepository.findAllBy(getClassName(projectionName));
        if(allBy == null || allBy.isEmpty()){
            throw  new EntityNotFoundException("User not found.");
        }

        return allBy;
    }

    public Object getUserById(Long id, String projectionName) {
        Optional<?> userById = userRepository.findById(id,getClassName(projectionName));
//        Optional<?> userById=userRepository.findById(id);
        if(userById.isPresent()){
            return userById.get();
        }
        else {
            throw  new EntityNotFoundException("User not found.");
        }
    }

    public Object getUserByEmail(String email) {
        Optional<?> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()){
            return byEmail.get();
        }
        else {
            throw  new EntityNotFoundException("User not found.");
        }
    }
    public Object getUserByEmail(String email, String projectionName) {
        Optional<?> byEmail = userRepository.findByEmail(email, getClassName(projectionName));
        if(byEmail.isPresent()){
            return byEmail.get();
        }
        else {
            throw  new EntityNotFoundException("User not found.");
        }
    }
    private Class<?> getClassName(String projectionName) {

        try {
            return Class.forName(projectionPackage+""+projectionName);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(projectionPackage+"UserDTO");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public Object deleteUserById(Long id) {
        Optional<?> userById = userRepository.findById(id, getClassName("UserProjectionDTO"));
        if(userById.isPresent()){
            userRepository.deleteById(id);
            return userById.get();
        }
        else {
            throw  new EntityNotFoundException("User not found.");
        }
    }

    public RegisterUser createUser(RegisterUser registerUser) {
        if(userRepository.existsByEmail(registerUser.getEmail()))
            throw new UserAlreadyExistlException("User already register with us.");
        User user = util.convertObject(registerUser, User.class);
        Role role=new Role();
        role.setName(ERoles.ROLE_USER);
        role.setUser(user);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(encoder.encode(user.getPassword()));
        return util.convertObject(userRepository.save(user), RegisterUser.class);
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(principal);
            return ResponseEntity.ok(new JwtResponse(jwt));
        }catch (Exception e){
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public String sendOtpToEmail(String email) {
//        String changePasswordUser = new ObjectMapper().writeValueAsString((ChangePasswordUser) getUserByEmail(email, "ChangePasswordUser"));
//        System.out.println(changePasswordUser);
//        User userPersonal = util.convertObject((ChangePasswordUser) getUserByEmail(email, "ChangePasswordUser"), User.class);
        User userPersonal = (User) getUserByEmail(email);
        String otp = util.generateOtp();
        if(util.sendOtp(email,userPersonal.getFirstName()+" "+userPersonal.getLastName(),otp)){
            userPersonal.setOtp(new Otp(otp,false,userPersonal));
            userRepository.save(userPersonal);
        }
        return "Otp sent sucessfully";
    }

    public String verifyOtpToEmail(String email, String otp) {
        User user = (User) getUserByEmail(email);
        Otp otpDTO = user.getOtp();
        if(otpDTO == null)
            throw new EmailException("Please request a new one.");

        if (otpDTO.isVerified() == true)
            throw new EmailException("The OTP already verified.");

        if(!otp.equals(otpDTO.getCode()))
            throw new EmailException("Invalid OTP. Please check the code and try again.");

        // check otp to ensure it has not expired
        long timeLapse = new Date().getTime() - otpDTO.getCreatedAt().getTime();
        // ensure the time lapse is not greater than 15 mins in milliseconds
        if(timeLapse >= 900000)
            throw new EmailException("The OTP has expired. Please request a new one.");

        otpDTO.setVerified(true);
        user.setOtp(otpDTO);
        userRepository.save(user);

        return "OTP verification successful";
    }

    public String changePassword(ChangePasswordDTO passwordDTO) {
        User userByEmail = (User)getUserByEmail(passwordDTO.getEmail());
        Otp otpDTO = userByEmail.getOtp();

        if(otpDTO == null)
            throw new EmailException("Please request for a new OTP.");

        if(!otpDTO.isVerified())
            throw new EmailException("OTP is not verified. Please resend otp and try again.");

        // ensure the time lapse is not greater than 15 mins in milliseconds
        if(new Date().getTime() - otpDTO.getCreatedAt().getTime() >= 900000)
            throw new EmailException("The One-Time Password (OTP) has expired. Please request a new one.");

        if(passwordEncoder.matches(passwordDTO.getPassword(),userByEmail.getPassword()))
            throw new EmailException("Old password and new password are same.");


        userByEmail.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        userByEmail.setOtp(null);
        userRepository.save(userByEmail);
        return "Password updated successfully";
    }


    public ApiResponse<Object> verifyJwtToken(String token) {
        jwtUtils.validateJwtToken(token);
        return new ApiResponse<Object>(HttpStatus.OK.value(),"Token is verified");
    }

    public void createDefaultUser(){
        String defEmail="default@gmail.com";
        if (!userRepository.existsByEmail(defEmail)){
            User user=new User();
            user.setEmail(defEmail);
            user.setMobile("1234567897");
            user.setFirstName("Pradip");
            user.setLastName("Chavda");
            user.setGender("Male");
            user.setPassword(passwordEncoder.encode("99097@Pradip"));
            user.setLocked(false);
            user.setEnabled(true);

            Address address=new Address();
            address.setGeneralAddress("Shankar para, Khas road, Botad");
            address.setCountry("India");
            address.setState("Gujarat");
            address.setPincode(364710);
            address.setUser(user);

            Role role=new Role();
            role.setName(ERoles.ROLE_ADMIN);
            role.setUser(user);

            user.setAddress(address);
            user.setRoles(Collections.singletonList(role));
            userRepository.save(user);
            log.info("Default User Created Successfully.");
        }
    }
}
