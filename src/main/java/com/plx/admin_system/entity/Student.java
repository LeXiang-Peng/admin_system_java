package com.plx.admin_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Schema(name = "Student", description = "")
public class Student extends User {

    private static final long serialVersionUID = 1L;

    @TableId(value = "student_id", type = IdType.AUTO)
    private Integer studentId;

    private String studentName;

    private String studentEmail;

    private Integer professionId;

    private Integer identificationNumber;

    private String studentPassword;

    private String studentPhoneNumber;

    private String studentGender;

    public void setStudentId(Integer studentId) {
        super.setUserId(studentId);
        this.studentId = studentId;
    }

    public void setStudentPassword(String studentPassword) {
        super.setPassword(studentPassword);
        this.studentPassword = studentPassword;
    }

}
