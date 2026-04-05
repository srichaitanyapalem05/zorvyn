package com.finance.dashboard.mapper;

import com.finance.dashboard.dto.UserResponseDTO;
import com.finance.dashboard.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole().getName())
                .build();
    }
}
