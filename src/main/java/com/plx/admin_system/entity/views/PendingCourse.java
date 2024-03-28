package com.plx.admin_system.entity.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class PendingCourse {
    private Integer id;
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
    private Byte isScheduled;
}
