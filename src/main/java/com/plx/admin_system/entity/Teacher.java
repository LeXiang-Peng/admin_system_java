package com.plx.admin_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author plx
 * @since 2024-02-27
 */
@Getter
@Setter
@Schema(name = "Teacher", description = "")
public class Teacher extends User {

    private static final long serialVersionUID = 1L;

    @TableId(value = "teacher_id", type = IdType.AUTO)
    private Integer teacherId;

    private String teacherName;

    private String teacherEmail;
    @JsonIgnore
    private String teacherPassword;

    private String teacherPhoneNumber;

    private String teacherGender;

    public void setTeacherId(Integer teacherId) {
        super.setUserId(teacherId);
        this.teacherId = teacherId;
    }

    public void setTeacherPassword(String teacherPassword) {
        super.setPassword(teacherPassword);
        this.teacherPassword = teacherPassword;
    }

//    public void setTeacherName(String teacherName) {
//        super.setUserName(teacherName);
//        this.teacherName = teacherName;
//    }
}
