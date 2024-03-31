package com.plx.admin_system.entity.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class SelectedCourse {
    private Integer id;
    @JsonProperty("course_id")
    private Integer courseId;
    private String course;
    private Byte type;
    private String clazz;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    private String lecturer;
    private String category;
    private Integer hours;
    private Float credits;
    private String semester;
    private String remark;
    @JsonProperty("is_scheduled")
    private Byte isScheduled;
    private Integer studentId;
    private String student;
}
