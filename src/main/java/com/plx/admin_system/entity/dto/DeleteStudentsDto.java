package com.plx.admin_system.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author plx
 */
@Data
public class DeleteStudentsDto {
    @JsonProperty("id")
    List<Integer> id;
    String password;
}
