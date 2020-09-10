package org.coderinfo.pf.admin.controller;

import org.coderinfo.pf.common.api.CommonResult;
import org.coderinfo.pf.admin.dto.UacPermissionNode;
import org.coderinfo.pf.core.domain.uac.UacPermission;
import org.coderinfo.pf.admin.service.UacPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户权限管理
 * Created by macro on 2018/9/29.
 */
@Controller
@Api(tags = "UacPermissionController", description = "后台用户权限管理")
@RequestMapping("/permission")
public class UacPermissionController {
    @Autowired
    private UacPermissionService permissionService;
    @ApiOperation("添加权限")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UacPermission permission) {
        int count = permissionService.create(permission);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改权限")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UacPermission permission) {
        int count = permissionService.update(id,permission);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据id批量删除权限")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = permissionService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("以层级结构返回所有权限")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UacPermissionNode>> treeList() {
        List<UacPermissionNode> permissionNodeList = permissionService.treeList();
        return CommonResult.success(permissionNodeList);
    }

    @ApiOperation("获取所有权限列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UacPermission>> list() {
        List<UacPermission> permissionList = permissionService.list();
        return CommonResult.success(permissionList);
    }
}
