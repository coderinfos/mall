package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.core.domain.uac.UacResource;

import java.util.List;

/**
 * 后台资源管理Service
 * Created by macro on 2020/2/2.
 */
public interface UacResourceService {
    /**
     * 添加资源
     */
    int create(UacResource UacResource);

    /**
     * 修改资源
     */
    int update(Long id, UacResource UacResource);

    /**
     * 获取资源详情
     */
    UacResource getItem(Long id);

    /**
     * 删除资源
     */
    int delete(Long id);

    /**
     * 分页查询资源
     */
    List<UacResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

    /**
     * 查询全部资源
     */
    List<UacResource> listAll();
}
