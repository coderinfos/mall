package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.admin.dto.UacMenuNode;
import org.coderinfo.pf.core.domain.uac.UacMenu;

import java.util.List;

/**
 * 后台菜单管理Service
 * Created by macro on 2020/2/2.
 */
public interface UacMenuService {
    /**
     * 创建后台菜单
     */
    int create(UacMenu UacMenu);

    /**
     * 修改后台菜单
     */
    int update(Long id, UacMenu UacMenu);

    /**
     * 根据ID获取菜单详情
     */
    UacMenu getItem(Long id);

    /**
     * 根据ID删除菜单
     */
    int delete(Long id);

    /**
     * 分页查询后台菜单
     */
    List<UacMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<UacMenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    int updateHidden(Long id, Integer hidden);
}
