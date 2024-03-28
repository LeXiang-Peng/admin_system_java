package com.plx.admin_system.entity.views;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author plx
 */
@Data
public class StudentView implements Serializable {
    @Excel(name = "学号", width = 10.0, orderNum = "0")
    private Integer id;
    @Excel(name = "姓名", width = 20.0, orderNum = "1")
    private String name;
    @Excel(name = "班级", width = 30.0, orderNum = "2")
    private String clazz;
    @Excel(name = "专业", width = 25.0, orderNum = "3")
    private String profession;
    @Excel(name = "院系", width = 25.0, orderNum = "4")
    private String department;
    @Excel(name = "性别", width = 10.0, orderNum = "5")
    private String gender;
    @Excel(name = "学年", width = 25.0, orderNum = "5")
    private String grade;
}
