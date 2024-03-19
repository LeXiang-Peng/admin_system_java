package com.plx.admin_system.utils.pojo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.plx.admin_system.entity.views.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author plx
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuList {
    private Integer menuId;
    private String menuName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuList> menuChildren;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String menuUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String menuIcon;
    //Constructor
    public MenuList(Menu menuView){
        this.menuIcon = menuView.getMenuIcon();
        this.menuId = menuView.getMenuId();
        this.menuName = menuView.getMenuName();
        this.menuUrl = menuView.getMenuUrl();
        this.menuChildren= new ArrayList<>();
    }
}