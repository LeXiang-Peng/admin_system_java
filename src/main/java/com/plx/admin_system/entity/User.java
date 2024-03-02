package com.plx.admin_system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author plx
 */
@Getter
@Setter
public class User implements Serializable {
    @JsonIgnore
    @TableField(exist = false)
    private Integer userId;
//    private String userName;
    @JsonIgnore
    @TableField(exist = false)
    private String password;
}
