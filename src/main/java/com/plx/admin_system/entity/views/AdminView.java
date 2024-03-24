package com.plx.admin_system.entity.views;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author plx
 */
@Data
public class AdminView {
    @Excel(name = "管理员ID", width = 20.0, orderNum = "0")
    private Integer id;
    @Excel(name = "姓名", width = 15.0, orderNum = "1")
    private String name;
    @Excel(name = "性别", width = 10.0, orderNum = "3")
    private String gender;
}
