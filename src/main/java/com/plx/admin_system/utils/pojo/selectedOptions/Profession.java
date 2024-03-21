package com.plx.admin_system.utils.pojo.selectedOptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author plx
 */
@Data
public class Profession {
    @JsonIgnore
    private String department;
    private String profession;
    List<Clazz> clazzList;

    public Profession(String department,String profession, String clazz) {
        this.department = department;
        this.profession = profession;
        clazzList = new ArrayList<>();
        clazzList.add(new Clazz(clazz));
    }
}
