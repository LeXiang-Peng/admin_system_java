package com.plx.admin_system.utils.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author plx
 */
@Data
public class Options {
    private String department;
    List<Profession> professionList;

    public Options(Profession profession) {
        this.department = profession.getDepartment();
        professionList = new ArrayList<>();
        professionList.add(profession);
    }
}
