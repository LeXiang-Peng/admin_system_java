package com.plx.admin_system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author plx
 */
@Getter
@Setter
@JsonIgnoreProperties({ "userId", "password" })
public class User implements Serializable {
    @TableField(exist = false)
    private Integer userId;
    @TableField(exist = false)
    private String userPassword;
}
