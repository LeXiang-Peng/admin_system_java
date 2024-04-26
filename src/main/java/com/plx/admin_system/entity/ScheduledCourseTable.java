package com.plx.admin_system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plx.admin_system.entity.dto.ChangeCourseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author plx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledCourseTable {
    @JsonProperty("id")
    private Integer scheduledCourseId;
    @JsonProperty("course_id")
    private Integer courseId;
    @JsonProperty("course")
    private String courseName;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    private String lecturer;
    @JsonProperty("classroom")
    private String classroomName;
    @JsonProperty("building")
    private String buildingName;
    @JsonProperty("weekday")
    private String weekDay;
    @JsonProperty("course_time")
    private String courseTime;
    private String semester;
    private Integer weeksTotal;
    private Integer timesOnceAWeek;
    private Integer currentTimes;
    private Integer totalTimes;
    private Boolean isRescheduled;

    public ScheduledCourseTable(ChangeCourseDto data) {
        this.courseId = data.getCourseId();
        this.courseName = data.getCourse();
        this.lecturerId = data.getLecturerId();
        this.lecturer = data.getLecturer();
        this.classroomName = data.getClassroom();
        this.buildingName = data.getBuilding();
        this.weekDay = data.getWeekday();
        this.courseTime = data.getCourseTime();
        this.isRescheduled = true;
    }
}
