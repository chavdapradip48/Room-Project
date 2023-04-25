package com.pradip.roommanagementsystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pradip.roommanagementsystem.exception.SmtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Component
public class GeneralUtil {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final int OTP_LENGTH = 6;

    public String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public boolean sendOtp(String to, String userName,String otp)  {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =  new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Your One-Time Password (OTP) for Room Management System");
            helper.setText("Dear "+userName+",\n\n"
                    + "Thank you for using [Your Application Name]. To ensure the security of your account, we have sent you a one-time password (OTP) to complete your action. Please use the following OTP to proceed:\n\n"
                    + otp + "\n\n"
                    + "Please note that this OTP will expire in [Expiry Time]. If you did not request this OTP, please ignore this email and contact us immediately at [Your Contact Email/Phone Number].\n\n"
                    + "Thank you for your cooperation in keeping your account secure.\n\n"
                    + "Sincerely,\n\n"
                    + "[Your Company Name]");

            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            throw new SmtpException("Error while sending OTP to user.");
        }
    }

    public <T, U> U convertObject(T sourceObject, Class<U> destinationObjectType) {
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, destinationObjectType.);
        return mapper.convertValue(sourceObject, destinationObjectType);
    }


}
