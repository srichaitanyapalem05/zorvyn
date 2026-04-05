package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.AuthRequestDTO;
import com.finance.dashboard.dto.AuthResponseDTO;
import com.finance.dashboard.security.CustomUserDetailsService;
import com.finance.dashboard.security.JwtUtil;
import com.finance.dashboard.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO login(AuthRequestDTO dto) {
        log.info("Login attempt for email: {}", dto.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        log.info("Login successful for email: {}, role: {}", dto.getEmail(), role);
        return new AuthResponseDTO(token, dto.getEmail(), role);
    }
}
