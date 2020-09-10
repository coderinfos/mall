package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.core.domain.uac.UacAdmin;
import org.coderinfo.pf.core.domain.uac.UacResource;

import java.util.List;

/**
 * 后台用户缓存操作类
 * Created by macro on 2020/3/13.
 */
public interface UacAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);

    /**
     * 删除后台用户资源列表缓存
     */
    void delResourceList(Long adminId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRole(Long roleId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRoleIds(List<Long> roleIds);

    /**
     * 当资源信息改变时，删除资源项目后台用户缓存
     */
    void delResourceListByResource(Long resourceId);

    /**
     * 获取缓存后台用户信息
     */
    UacAdmin getAdmin(String username);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UacAdmin admin);

    /**
     * 获取缓存后台用户资源列表
     */
    List<UacResource> getResourceList(Long adminId);

    /**
     * 设置后台后台用户资源列表
     */
    void setResourceList(Long adminId, List<UacResource> resourceList);
}
