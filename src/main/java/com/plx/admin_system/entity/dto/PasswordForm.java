package com.plx.admin_system.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class PasswordForm {
    @JsonProperty("oldPassword")
    private String oldPassword;
    @JsonProperty("newPassword")
    private String newPassword;
}
