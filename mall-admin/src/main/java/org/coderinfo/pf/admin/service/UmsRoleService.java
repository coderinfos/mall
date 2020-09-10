package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.core.domain.uac.UacMenu;
import org.coderinfo.pf.core.domain.uac.UacPermission;
import org.coderinfo.pf.core.domain.uac.UacResource;
import org.coderinfo.pf.core.domain.uac.UacRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台角色管理Service
 * Created by macro on 2018/9/30.
 */
public interface UacRoleService {
    /**
     * 添加角色
     */
    int create(UacRole role);

    /**
     * 修改角色信息
     */
    int update(Long id, UacRole role);

    /**
     * 批量删除角色
     */
    int delete(List<Long> ids);

    /**
     * 获取指定角色权限
     */
    List<UacPermission> getPermissionList(Long roleId);

    /**
     * 修改指定角色的权限
     */
    @Transactional
    int updatePermission(Long roleId, List<Long> permissionIds);

    /**
     * 获取所有角色列表
     */
    List<UacRole> list();

    /**
     * 分页获取角色列表
     */
    List<UacRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<UacMenu> getMenuList(Long adminId);

    /**
     * 获取角色相关菜单
     */
    List<UacMenu> listMenu(Long roleId);

    /**
     * 获取角色相关资源
     */
    List<UacResource> listResource(Long roleId);

    /**
     * 给角色分配菜单
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 给角色分配资源
     */
    @Transactional
    int allocResource(Long roleId, List<Long> resourceIds);
}
