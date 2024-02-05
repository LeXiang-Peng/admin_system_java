package com.plx.admin_system.mapper;

import com.plx.admin_system.entity.views.Menu;
import org.apache.ibatis.annotations.MapKey;

import java.util.HashMap;
import java.util.List;

/**
 * @author plx
 */
public interface CommonMapper {
    @MapKey("menuId")
    HashMap<Integer,Menu> getAdminMenuView();
}
