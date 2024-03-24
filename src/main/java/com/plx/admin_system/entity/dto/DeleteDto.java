package com.plx.admin_system.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author plx
 */
@Data
public class DeleteDto {
    @JsonProperty("id")
    List<Integer> id;
    @JsonProperty("password")
    String password;
}
