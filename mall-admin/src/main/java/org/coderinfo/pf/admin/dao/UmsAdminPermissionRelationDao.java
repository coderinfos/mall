package org.coderinfo.pf.admin.dao;

import org.coderinfo.pf.core.domain.uac.UacAdminPermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义用户权限关系管理Dao
 * Created by macro on 2018/10/8.
 */
public interface UacAdminPermissionRelationDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<UacAdminPermissionRelation> list);
}
