package com.plx.admin_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
@Getter
@Setter
@ToString
@Schema(name = "Admin", description = "")
public class Admin extends User {

    private static final long serialVersionUID = 1L;

    @TableId(value = "admin_id", type = IdType.AUTO)
    @JsonProperty("id")
    private Integer adminId;
    @JsonProperty("name")
    private String adminName;

    private String adminEmail;
    @JsonIgnore
    private String adminPassword;

    private String adminPhoneNumber;
    @JsonProperty("gender")
    private String adminGender;

    private Byte adminType;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    public void setAdminId(Integer adminId) {
        super.setUserId(adminId);
        this.adminId = adminId;
    }

    public void setAdminPassword(String adminPassword) {
        super.setUserPassword(adminPassword);
        this.adminPassword = adminPassword;
    }

    public void setAdminName(String adminName) {
        super.setUserName(adminName);
        this.adminName = adminName;
    }

    @Override
    public void setAvatarUrl(String avatarUrl) {
        super.setAvatarUrl(avatarUrl);
        this.avatarUrl = avatarUrl;
    }
}
