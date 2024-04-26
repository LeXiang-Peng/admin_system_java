package com.plx.admin_system.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Clazz {
    @JsonProperty("id")
    @Excel(name = "班级编号", width = 10.0, orderNum = "0")
    private Integer clazzId;
    @JsonProperty("clazz")
    @Excel(name = "班级", width = 30.0, orderNum = "1")
    private String clazzName;
    @JsonProperty("profession")
    @Excel(name = "专业", width = 30.0, orderNum = "2")
    private String professionName;
    @JsonProperty("department")
    @Excel(name = "院系", width = 25.0, orderNum = "3")
    private String departmentName;
    @JsonProperty("semester")
    @Excel(name = "学期", width = 20.0, orderNum = "4")
    private String currentSemester;
    @JsonProperty("total")
    @Excel(name = "班级人数", width = 15.0, orderNum = "5")
    private Integer studentTotal;
}
