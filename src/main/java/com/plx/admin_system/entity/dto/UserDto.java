package com.plx.admin_system.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author plx
 */
@Data
@ToString
public class UserDto {
    private Integer id;
    private String password;
    private String captcha;
    private String role;
}
