package com.plx.admin_system.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class InfoDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("email")
    private String email;
    @JsonProperty("IDNumber")
    private String IDNumber;
}
