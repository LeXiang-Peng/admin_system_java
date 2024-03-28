package com.plx.admin_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author plx
 * @since 2024-03-26
 */
@Getter
@Setter
@TableName("approvaling_course")
@Schema(name = "ApprovalingCourse", description = "")
@ToString
public class ApprovalingCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "approvaling_course_id", type = IdType.AUTO)
    @JsonProperty("id")
    private Integer approvalingCourseId;
    @JsonProperty("course")
    private String courseName;
    @JsonProperty("type")
    private Byte courseType;
    @JsonProperty("clazz")
    private String clazzName;
    private Integer lecturerId;
    private String lecturer;
    private String category;
    private String remark;
    private String semester;
    private Byte status;
}
