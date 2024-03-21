package com.plx.admin_system.entity.views;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author plx
 */
@Data
public class StudentList implements Serializable {
    @Excel(name = "学号", width = 10.0)
    private Integer id;
    @Excel(name = "姓名", width = 20.0)
    private String name;
    @Excel(name = "班级", width = 30.0)
    private String clazz;
    @Excel(name = "专业", width = 25.0)
    private String profession;
    @Excel(name = "院系", width = 25.0)
    private String department;
    @Excel(name = "性别", width = 10.0)
    private String gender;
}
