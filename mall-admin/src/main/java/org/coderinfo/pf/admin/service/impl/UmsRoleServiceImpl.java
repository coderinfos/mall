package org.coderinfo.pf.admin.service.impl;

import com.github.pagehelper.PageHelper;
import org.coderinfo.pf.admin.dao.UacRoleDao;
import org.coderinfo.pf.admin.dao.UacRolePermissionRelationDao;
import org.coderinfo.pf.core.domain.uac.*;
import org.coderinfo.pf.core.mapper.UacRoleMapper;
import org.coderinfo.pf.core.mapper.UacRoleMenuRelationMapper;
import org.coderinfo.pf.core.mapper.UacRolePermissionRelationMapper;
import org.coderinfo.pf.core.mapper.UacRoleResourceRelationMapper;
import com.macro.mall.model.*;
import org.coderinfo.pf.admin.service.UacAdminCacheService;
import org.coderinfo.pf.admin.service.UacRoleService;
import org.coderinfo.pf.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台角色管理Service实现类
 * Created by macro on 2018/9/30.
 */
@Service
public class UacRoleServiceImpl implements UacRoleService {
    @Autowired
    private UacRoleMapper roleMapper;
    @Autowired
    private UacRolePermissionRelationMapper rolePermissionRelationMapper;
    @Autowired
    private UacRoleMenuRelationMapper roleMenuRelationMapper;
    @Autowired
    private UacRoleResourceRelationMapper roleResourceRelationMapper;
    @Autowired
    private UacRolePermissionRelationDao rolePermissionRelationDao;
    @Autowired
    private UacRoleDao roleDao;
    @Autowired
    private UacAdminCacheService adminCacheService;
    @Override
    public int create(UacRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        return roleMapper.insert(role);
    }

    @Override
    public int update(Long id, UacRole role) {
        role.setId(id);
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int delete(List<Long> ids) {
        UacRoleExample example = new UacRoleExample();
        example.createCriteria().andIdIn(ids);
        int count = roleMapper.deleteByExample(example);
        adminCacheService.delResourceListByRoleIds(ids);
        return count;
    }

    @Override
    public List<UacPermission> getPermissionList(Long roleId) {
        return rolePermissionRelationDao.getPermissionList(roleId);
    }

    @Override
    public int updatePermission(Long roleId, List<Long> permissionIds) {
        //先删除原有关系
        UacRolePermissionRelationExample example=new UacRolePermissionRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        rolePermissionRelationMapper.deleteByExample(example);
        //批量插入新关系
        List<UacRolePermissionRelation> relationList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            UacRolePermissionRelation relation = new UacRolePermissionRelation();
            relation.setRoleId(roleId);
            relation.setPermissionId(permissionId);
            relationList.add(relation);
        }
        return rolePermissionRelationDao.insertList(relationList);
    }

    @Override
    public List<UacRole> list() {
        return roleMapper.selectByExample(new UacRoleExample());
    }

    @Override
    public List<UacRole> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UacRoleExample example = new UacRoleExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return roleMapper.selectByExample(example);
    }

    @Override
    public List<UacMenu> getMenuList(Long adminId) {
        return roleDao.getMenuList(adminId);
    }

    @Override
    public List<UacMenu> listMenu(Long roleId) {
        return roleDao.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UacResource> listResource(Long roleId) {
        return roleDao.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        UacRoleMenuRelationExample example=new UacRoleMenuRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleMenuRelationMapper.deleteByExample(example);
        //批量插入新关系
        for (Long menuId : menuIds) {
            UacRoleMenuRelation relation = new UacRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            roleMenuRelationMapper.insert(relation);
        }
        return menuIds.size();
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        //先删除原有关系
        UacRoleResourceRelationExample example=new UacRoleResourceRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleResourceRelationMapper.deleteByExample(example);
        //批量插入新关系
        for (Long resourceId : resourceIds) {
            UacRoleResourceRelation relation = new UacRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            roleResourceRelationMapper.insert(relation);
        }
        adminCacheService.delResourceListByRole(roleId);
        return resourceIds.size();
    }
}
