package com.plx.admin_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Schema(name = "Teacher", description = "")
@ToString
public class Teacher extends User {

    private static final long serialVersionUID = 1L;

    @TableId(value = "teacher_id", type = IdType.AUTO)
    private Integer teacherId;

    private String teacherName;

    private String departmentName;

    private String teacherEmail;
    @JsonIgnore
    private String teacherPassword;

    private String teacherPhoneNumber;

    private String teacherGender;

    private Byte isAuthorized;

    private Byte isExisted;

    public void setTeacherId(Integer teacherId) {
        super.setUserId(teacherId);
        this.teacherId = teacherId;
    }

    public void setTeacherPassword(String teacherPassword) {
        super.setUserPassword(teacherPassword);
        this.teacherPassword = teacherPassword;
    }
}
