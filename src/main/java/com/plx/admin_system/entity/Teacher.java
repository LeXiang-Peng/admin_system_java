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
@Schema(name = "Teacher", description = "")
@ToString
public class Teacher extends User {

    private static final long serialVersionUID = 1L;

    @TableId(value = "teacher_id", type = IdType.AUTO)
    @JsonProperty("id")
    private Integer teacherId;
    @JsonProperty("name")
    private String teacherName;
    @JsonProperty("department")
    private String departmentName;

    private String teacherEmail;
    @JsonIgnore
    private String teacherPassword;

    private String teacherPhoneNumber;
    @JsonProperty("gender")
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

    public void setTeacherName(String teacherName) {
        super.setUserName(teacherName);
        this.teacherName = teacherName;
    }
}
