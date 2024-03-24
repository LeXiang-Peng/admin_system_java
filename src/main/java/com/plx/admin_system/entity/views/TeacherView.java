package com.plx.admin_system.entity.views;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author plx
 */
@Data
public class TeacherView implements Serializable {
    @Excel(name = "教师编号", width = 10.0, orderNum = "0")
    private Integer id;
    @Excel(name = "姓名", width = 20.0, orderNum = "1")
    private String name;
    @Excel(name = "院系", width = 25.0, orderNum = "2")
    private String department;
    @Excel(name = "性别", width = 10.0, orderNum = "3")
    private String gender;
}
