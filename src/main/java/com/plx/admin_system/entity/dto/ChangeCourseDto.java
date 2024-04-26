package com.plx.admin_system.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author plx
 */
@Data
@ToString
public class ChangeCourseDto {
    private Integer id;
    @JsonProperty("course_id")
    private Integer courseId;
    private String course;
    private Integer lecturerId;
    private String lecturer;
    private String classroom;
    private String building;
    @JsonProperty("change_weekday")
    private String changeWeekday;
    @JsonProperty("change_course_time")
    private String changeCourseTime;
    @JsonProperty("change_week")
    private Integer changeWeek;
    @JsonProperty("curr_week")
    private Integer currWeek;
    @JsonProperty("course_time")
    private String courseTime;
    private String weekday;
}
