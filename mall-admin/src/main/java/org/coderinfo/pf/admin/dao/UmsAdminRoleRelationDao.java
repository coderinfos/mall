package org.coderinfo.pf.admin.dao;

import org.coderinfo.pf.core.domain.uac.UacAdminRoleRelation;
import org.coderinfo.pf.core.domain.uac.UacPermission;
import org.coderinfo.pf.core.domain.uac.UacResource;
import org.coderinfo.pf.core.domain.uac.UacRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义后台用户与角色管理Dao
 * Created by macro on 2018/10/8.
 */
public interface UacAdminRoleRelationDao {
    /**
     * 批量插入用户角色关系
     */
    int insertList(@Param("list") List<UacAdminRoleRelation> adminRoleRelationList);

    /**
     * 获取用于所有角色
     */
    List<UacRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有角色权限
     */
    List<UacPermission> getRolePermissionList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有权限(包括+-权限)
     */
    List<UacPermission> getPermissionList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有可访问资源
     */
    List<UacResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
