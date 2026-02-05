package com.databridge.auth_service.service.implimentations;

import com.databridge.auth_service.dtos.*;
import com.databridge.auth_service.entity.RefreshToken;
import com.databridge.auth_service.entity.User;
import com.databridge.auth_service.enums.AuthProvider;
import com.databridge.auth_service.enums.ErrorCode;
import com.databridge.auth_service.enums.Role;
import com.databridge.auth_service.exception.AppException;
import com.databridge.auth_service.repository.UserRepository;
import com.databridge.auth_service.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RedisTemplate<String, Object> redisTemplate;



    @Override
    public RegisterResponse register(RegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String key = "register:" + request.getEmail();

        redisTemplate.opsForValue().set(
                key,
                request,
                Duration.ofMinutes(5)
        );


        String otp = otpService.generateOtp(request.getEmail());

        emailService.sendOtp(request.getEmail(), otp);

        return RegisterResponse.builder()
                .message("OTP sent to your email. Please verify.")
                .build();
    }


    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .build();
    }



    @Override
    public AuthResponse refresh(RefreshRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken());

        User user = userRepository.findById(
                refreshToken.getUserId()
        ).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(user.getRole().name())
                .build();
    }



    @Override
    public AuthResponse verifyOtp(VerifyOtpRequest request){

        boolean valid =
                otpService.verifyOtp(request.getEmail(), request.getOtp());

        if(!valid){
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        String key = "register:" + request.getEmail();

        RegisterRequest registerRequest =
                (RegisterRequest) redisTemplate.opsForValue().get(key);

        if(registerRequest == null){
            throw new AppException(ErrorCode.REGISTRATION_EXPIRED);
        }


        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.LOCAL);
        user.setCreatedAt(Instant.now());


        userRepository.save(user);

        redisTemplate.delete(key);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .build();
    }
}
