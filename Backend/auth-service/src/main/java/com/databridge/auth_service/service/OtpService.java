package com.databridge.auth_service.service;

import com.databridge.auth_service.enums.ErrorCode;
import com.databridge.auth_service.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int OTP_EXPIRY_MINUTES = 5;

    public String generateOtp(String email){

        String otp = String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 999999)
        );

        String hashedOtp = BCrypt.hashpw(otp, BCrypt.gensalt());

        redisTemplate.opsForValue().set(
                "otp:" + email,
                hashedOtp,
                Duration.ofMinutes(OTP_EXPIRY_MINUTES)
        );

        return otp;
    }

    public boolean verifyOtp(String email, String otp){

        String key = "otp:" + email;

        Object storedHash = redisTemplate.opsForValue().get(key);

        if(storedHash == null){
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        boolean matches = BCrypt.checkpw(otp, storedHash.toString());

        if(matches){
            redisTemplate.delete(key);
        }

        return matches;
    }
}
