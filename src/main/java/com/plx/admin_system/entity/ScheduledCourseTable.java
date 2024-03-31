package com.plx.admin_system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class ScheduledCourseTable {
    @JsonProperty("id")
    private Integer scheduledCourseId;
    @JsonProperty("course_id")
    private Integer courseId;
    @JsonProperty("course")
    private String courseName;
    @JsonProperty("lecture_id")
    private Integer lecturerId;
    private String lecturer;
    @JsonProperty("classroom")
    private String classroomName;
    @JsonProperty("building")
    private String buildingName;
    @JsonProperty("weekday")
    private String weekDay;
    @JsonProperty("create_time")
    private Integer courseTime;
    private String semester;
}
