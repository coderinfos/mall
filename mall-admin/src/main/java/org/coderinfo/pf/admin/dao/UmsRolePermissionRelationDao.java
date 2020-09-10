package org.coderinfo.pf.admin.dao;

import org.coderinfo.pf.core.domain.uac.UacPermission;
import org.coderinfo.pf.core.domain.uac.UacRolePermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义角色权限关系管理Dao
 * Created by macro on 2018/9/30.
 */
public interface UacRolePermissionRelationDao {
    /**
     * 批量插入角色和权限关系
     */
    int insertList(@Param("list")List<UacRolePermissionRelation> list);

    /**
     * 根据角色获取权限
     */
    List<UacPermission> getPermissionList(@Param("roleId") Long roleId);
}
