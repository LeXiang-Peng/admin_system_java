package com.plx.admin_system.utils.pojo.schduledCourse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class ClassroomInfo {
    @JsonProperty("classroom")
    private String classroomName;
    @JsonProperty("building")
    private String buildingName;
    private Integer capacity;
}
