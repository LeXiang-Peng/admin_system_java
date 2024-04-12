package com.plx.admin_system.utils.pojo.schduledCourse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author plx
 */
@Data
@NoArgsConstructor
public class SchedulingCourse {
    @JsonProperty("id")
    private Integer courseId;
    private String course;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    private String lecturer;
    private Integer hours;
    private String semester;
    private List<String> clazzList;
    @JsonProperty("student_total")
    private Integer studentTotal;

    public SchedulingCourse(SchedulingCourse courseInfo) {
        this.courseId = courseInfo.getCourseId();
        this.course = courseInfo.getCourse();
        this.lecturerId = courseInfo.getLecturerId();
        this.lecturer = courseInfo.getLecturer();
        this.hours = courseInfo.getHours();
        this.semester = courseInfo.getSemester();
        this.clazzList = new ArrayList<>(courseInfo.getClazzList());
        this.studentTotal = courseInfo.getStudentTotal();
    }
}
