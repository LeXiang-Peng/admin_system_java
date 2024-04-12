package com.plx.admin_system.entity.views;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author plx
 */
@Data
public class ScheduledCourseInfo {
    private Integer courseId;
    private String courseName;
    private Integer lecturerId;
    private String lecturer;
    private List<String> clazzList;
    private String classroom;
    private String building;
    private String weekday;
    private String courseTime;
}
