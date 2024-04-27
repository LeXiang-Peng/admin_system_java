package com.plx.admin_system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author plx
 */
@Getter
@Setter
@ToString
public class User implements Serializable {
    @TableField(exist = false)
    private Integer userId;
    @TableField(exist = false)
    private String userPassword;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String avatarUrl;
}
