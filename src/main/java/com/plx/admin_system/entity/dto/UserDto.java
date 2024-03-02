package com.plx.admin_system.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author plx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private Integer id;
    private String password;
    private String code;
    private String role;
    private boolean isRemember;
}
