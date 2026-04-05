package com.finance.dashboard.dto;

import com.finance.dashboard.model.Role.RoleName;
import com.finance.dashboard.model.User.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private UserStatus status;
    private RoleName role;
}
