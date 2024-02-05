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
    private String menuName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String menuIcon;
    private List<MenuList> menuChildren;
    //Constructor
    public MenuList(Menu menuView){
        this.menuName = menuView.getMenuName();
        this.menuIcon = menuView.getMenuIcon();
        this.menuChildren= new ArrayList<>();
    }
}
