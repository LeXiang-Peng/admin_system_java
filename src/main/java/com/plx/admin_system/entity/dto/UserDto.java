package com.plx.admin_system.entity.dto;

import lombok.Data;
import lombok.ToString;

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
