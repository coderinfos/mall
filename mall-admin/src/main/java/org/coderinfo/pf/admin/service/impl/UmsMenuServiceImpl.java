package org.coderinfo.pf.admin.service.impl;

import com.github.pagehelper.PageHelper;
import org.coderinfo.pf.admin.dto.UacMenuNode;
import org.coderinfo.pf.core.mapper.UacMenuMapper;
import com.macro.mall.model.*;
import org.coderinfo.pf.admin.service.UacMenuService;
import org.coderinfo.pf.core.domain.uac.UacMenu;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
public class UacMenuServiceImpl implements UacMenuService {
    @Autowired
    private UacMenuMapper menuMapper;

    @Override
    public int create(UacMenu UacMenu) {
        UacMenu.setCreateTime(new Date());
        updateLevel(UacMenu);
        return menuMapper.insert(UacMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(UacMenu UacMenu) {
        if (UacMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            UacMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            UacMenu parentMenu = menuMapper.selectByPrimaryKey(UacMenu.getParentId());
            if (parentMenu != null) {
                UacMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                UacMenu.setLevel(0);
            }
        }
    }

    @Override
    public int update(Long id, UacMenu UacMenu) {
        UacMenu.setId(id);
        updateLevel(UacMenu);
        return menuMapper.updateByPrimaryKeySelective(UacMenu);
    }

    @Override
    public UacMenu getItem(Long id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        return menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<UacMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UacMenuExample example = new UacMenuExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return menuMapper.selectByExample(example);
    }

    @Override
    public List<UacMenuNode> treeList() {
        List<UacMenu> menuList = menuMapper.selectByExample(new UacMenuExample());
        List<UacMenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
        return result;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        UacMenu UacMenu = new UacMenu();
        UacMenu.setId(id);
        UacMenu.setHidden(hidden);
        return menuMapper.updateByPrimaryKeySelective(UacMenu);
    }

    /**
     * 将UacMenu转化为UacMenuNode并设置children属性
     */
    private UacMenuNode covertMenuNode(UacMenu menu, List<UacMenu> menuList) {
        UacMenuNode node = new UacMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<UacMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
