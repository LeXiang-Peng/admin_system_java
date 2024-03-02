package com.plx.admin_system.entity.views;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author plx
 */
@Getter
@Setter
@Schema(name = "admin_menu", description = "")
public class Menu {
    @TableId(value = "menu_id")
    private Integer menuId;
    private String menuName;
    private Integer parentMenuId;
}
