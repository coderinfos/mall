package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.admin.dto.UacPermissionNode;
import org.coderinfo.pf.core.domain.uac.UacPermission;

import java.util.List;

/**
 * 后台用户权限管理Service
 * Created by macro on 2018/9/29.
 */
public interface UacPermissionService {
    /**
     * 添加权限
     */
    int create(UacPermission permission);

    /**
     * 修改权限
     */
    int update(Long id,UacPermission permission);

    /**
     * 批量删除权限
     */
    int delete(List<Long> ids);

    /**
     * 以层级结构返回所有权限
     */
    List<UacPermissionNode> treeList();

    /**
     * 获取所有权限
     */
    List<UacPermission> list();
}
