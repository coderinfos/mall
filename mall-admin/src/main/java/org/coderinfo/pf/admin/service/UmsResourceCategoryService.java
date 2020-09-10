package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.core.domain.uac.UacResourceCategory;

import java.util.List;

/**
 * 后台资源分类管理Service
 * Created by macro on 2020/2/5.
 */
public interface UacResourceCategoryService {

    /**
     * 获取所有资源分类
     */
    List<UacResourceCategory> listAll();

    /**
     * 创建资源分类
     */
    int create(UacResourceCategory UacResourceCategory);

    /**
     * 修改资源分类
     */
    int update(Long id, UacResourceCategory UacResourceCategory);

    /**
     * 删除资源分类
     */
    int delete(Long id);
}
