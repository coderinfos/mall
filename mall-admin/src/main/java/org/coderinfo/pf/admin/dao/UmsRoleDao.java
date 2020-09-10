package org.coderinfo.pf.admin.dao;

import org.coderinfo.pf.core.domain.uac.UacMenu;
import org.coderinfo.pf.core.domain.uac.UacResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义后台角色管理Dao
 * Created by macro on 2020/2/2.
 */
public interface UacRoleDao {
    /**
     * 根据后台用户ID获取菜单
     */
    List<UacMenu> getMenuList(@Param("adminId") Long adminId);
    /**
     * 根据角色ID获取菜单
     */
    List<UacMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
    /**
     * 根据角色ID获取资源
     */
    List<UacResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}
