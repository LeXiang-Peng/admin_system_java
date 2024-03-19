package com.plx.admin_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@Schema(name = "Admin", description = "")
public class Admin extends User {

    private static final long serialVersionUID = 1L;

    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;

    private String adminName;

    private String departmentName;

    private String adminEmail;
    @JsonIgnore
    private String adminPassword;

    private String adminPhoneNumber;

    private String adminGender;

    private Byte adminType;

    public void setAdminId(Integer adminId) {
        super.setUserId(adminId);
        this.adminId = adminId;
    }

    public void setAdminPassword(String adminPassword) {
        super.setPassword(adminPassword);
        this.adminPassword = adminPassword;
    }
}
