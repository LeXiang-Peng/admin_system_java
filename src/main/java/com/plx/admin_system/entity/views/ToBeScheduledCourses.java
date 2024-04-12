package com.plx.admin_system.entity.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author plx
 */
@Data
public class ToBeScheduledCourses {
    private Integer id;
    private String course;
    private Byte type;
    private String category;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    private String lecturer;
    private String semester;
    private String clazz;
    @JsonProperty("student_total")
    private Integer studentTotal;
    private Integer hours;
}
