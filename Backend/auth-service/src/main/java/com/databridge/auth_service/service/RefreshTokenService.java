package com.databridge.auth_service.service;

import com.databridge.auth_service.entity.RefreshToken;
import com.databridge.auth_service.enums.ErrorCode;
import com.databridge.auth_service.exception.AuthException;
import com.databridge.auth_service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshExpiration;

    public String createRefreshToken(UUID userId){

        RefreshToken token = RefreshToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .build();

        refreshTokenRepository.save(token);

        return token.getToken();
    }
    public RefreshToken verifyRefreshToken(String token){

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new AuthException(ErrorCode.INVALID_TOKEN));

        if(refreshToken.isRevoked()){
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }

        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }

        return refreshToken;
    }

}
