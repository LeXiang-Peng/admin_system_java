package com.plx.admin_system.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author plx
 */
@Data
@NoArgsConstructor
public class Department {
    @JsonProperty("id")
    @Excel(name = "院系编号", width = 10.0, orderNum = "0")
    private Integer departmentId;
    @JsonProperty("department")
    @Excel(name = "院系", width = 30.0, orderNum = "1")
    private String departmentName;

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }
}
