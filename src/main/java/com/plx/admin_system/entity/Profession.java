package com.plx.admin_system.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class Profession {
    @JsonProperty("id")
    @Excel(name = "专业编号", width = 10.0, orderNum = "0")
    private Integer professionId;
    @JsonProperty("profession")
    @Excel(name = "专业", width = 30.0, orderNum = "1")
    private String professionName;
    @JsonProperty("department")
    @Excel(name = "院系", width = 25.0, orderNum = "2")
    private String departmentName;
}
