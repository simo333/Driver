package com.simo333.driver.controller;

import com.simo333.driver.exception.RefreshTokenException;
import com.simo333.driver.model.RefreshToken;
import com.simo333.driver.model.Role;
import com.simo333.driver.model.User;
import com.simo333.driver.payload.security.LoginRequest;
import com.simo333.driver.payload.security.RegisterRequest;
import com.simo333.driver.payload.security.UserInfoResponse;
import com.simo333.driver.security.email_verification.OnRegistrationCompleteEvent;
import com.simo333.driver.security.jwt.JwtUtils;
import com.simo333.driver.service.EmailVerificationService;
import com.simo333.driver.service.RefreshTokenService;
import com.simo333.driver.service.RoleService;
import com.simo333.driver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final ApplicationEventPublisher eventPublisher;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user.getUsername());
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        RefreshToken refreshToken = refreshTokenService.create(user.getUsername());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new UserInfoResponse(user.getUsername(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .roles(Set.of(roleService.findOne(Role.Type.ROLE_USER)))
                .enabled(false)
                .build();

        User registeredUser = userService.save(user);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser));

        log.info("User '{}' registered successfully.", user.getUsername());
        return ResponseEntity.ok(String.format(
                "User '%s' registered successfully. Check your email for account activation link.", user.getUsername()));
    }

    @GetMapping("/confirmEmail")
    public ResponseEntity<Object> confirmRegistration(@RequestParam("token") String token) {
        emailVerificationService.confirmRegistration(token);
        return ResponseEntity.ok("Account activated successfully.");
    }

    @PostMapping("/resend-confirmation-email")
    public ResponseEntity<Object> resendConfirmationEmail(@RequestParam String username) {
        User user = userService.findOne(username);
        if (user.isEnabled()) {
            return ResponseEntity.badRequest().body("User is already activated.");
        }
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        return ResponseEntity.ok("An email has been resent.");
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"anonymousUser".equals(principle.toString())) {
            refreshTokenService.deleteByUser((User) principle);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("You've been signed out.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String refreshTokenCookie = jwtUtils.getJwtRefreshFromCookies(request);
        if ((refreshTokenCookie != null) && (refreshTokenCookie.length() > 0)) {
            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenCookie);
            if (refreshTokenService.verifyExpiration(refreshToken)) {
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(refreshToken.getUser().getUsername());
                log.info("Token refreshed successfully.");
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body("Token refreshed successfully.");
            }
        }
        log.error("Refresh token is not in database. For: {}", refreshTokenCookie);
        throw new RefreshTokenException(refreshTokenCookie,
                "Refresh token has expired.");
    }
}
