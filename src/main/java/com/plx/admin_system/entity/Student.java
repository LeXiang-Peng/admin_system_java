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
@Schema(name = "Student", description = "")
public class Student extends User {

    private static final long serialVersionUID = 1L;

    @TableId(value = "student_id", type = IdType.AUTO)
    private Integer studentId;

    private String studentName;

    private String studentGender;

    private String studentPhoneNumber;

    private String studentEmail;

    private String clazzName;
    @JsonIgnore
    private String identificationNumber;
    @JsonIgnore
    private String studentPassword;

    private Byte isExisted;

    public void setStudentId(Integer studentId) {
        super.setUserId(studentId);
        this.studentId = studentId;
    }

    public void setStudentPassword(String studentPassword) {
        super.setPassword(studentPassword);
        this.studentPassword = studentPassword;
    }
}
