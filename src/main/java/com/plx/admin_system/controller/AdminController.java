package com.plx.admin_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.service.IAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author plx
 * @since 2024-03-05
 */
@RestController
@PreAuthorize("hasAuthority('admin+')")
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
    @PreAuthorize("hasAuthority('admin')")
    public Boolean deleteOne(@PathVariable Integer id) {
        return adminService.removeById(id);
    }

    @GetMapping("/queryOne/{id}")
    public ResponseResult queryOne(@PathVariable Integer id) {
        Admin admin = adminService.getById(id);
        return Objects.isNull(admin) ? new ResponseResult(HttpStatus.OK.value(), "用户不存在")
                : new ResponseResult(HttpStatus.OK.value(), "查询成功", admin);
    }

    @GetMapping("/pagingQuery")
    public Page<Admin> pagingQuery(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize) {
        return adminService.page(new Page<>(pageNum, pageSize));
    }
}
