package com.databridge.auth_service.security;

import com.databridge.auth_service.entity.User;
import com.databridge.auth_service.enums.AuthProvider;
import com.databridge.auth_service.enums.ErrorCode;
import com.databridge.auth_service.enums.Role;
import com.databridge.auth_service.exception.AuthException;
import com.databridge.auth_service.repository.UserRepository;
import com.databridge.auth_service.service.JwtService;
import com.databridge.auth_service.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String emailAttr = oAuth2User.getAttribute("email");

        if(emailAttr == null){
            throw new AuthException(ErrorCode.OAUTH_EMAIL_NOT_FOUND);
        }

        final String email = emailAttr.toLowerCase();


        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createOAuthUser(email));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

        String redirectUrl =
                frontendUrl + "/oauth-success" +
                        "?accessToken=" + accessToken +
                        "&refreshToken=" + refreshToken;

        response.sendRedirect(redirectUrl);
    }

    private User createOAuthUser(String email){

        User user = User.builder()
                .email(email)
                .provider(AuthProvider.GOOGLE)
                .role(Role.USER)
                .createdAt(Instant.now())
                .build();

        return userRepository.save(user);
    }
}
