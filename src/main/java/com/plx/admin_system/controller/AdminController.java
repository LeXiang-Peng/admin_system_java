 package com.plx.admin_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.plx.admin_system.service.IAdminService;
import com.plx.admin_system.entity.Admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author plx
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private IAdminService adminService;

    @GetMapping
    public List<Admin> queryAll() {
        return adminService.list();
    }
    @PostMapping("/saveOrUpdate")
    public Boolean saveOrUpdate(@RequestBody Admin admin) {
        return adminService.saveOrUpdate(admin);
    }
    @DeleteMapping("/deleteOne/{id}")
    public Boolean deleteOne(@PathVariable Integer id) {
        return adminService.removeById(id);
    }
    @GetMapping("/queryOne/{id}")
    @PreAuthorize("hasAuthority('admin+')")
    public Admin queryOne(@PathVariable Integer id) {
        return adminService.getById(id);
    }
    @GetMapping("/pagingQuery")
    public Page<Admin> pagingQuery(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        return adminService.page(new Page<>(pageNum, pageSize));
    }
}
