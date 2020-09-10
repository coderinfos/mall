package org.coderinfo.pf.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import org.coderinfo.pf.core.mapper.UacResourceMapper;
import org.coderinfo.pf.core.domain.uac.UacResource;
import org.coderinfo.pf.admin.service.UacAdminCacheService;
import org.coderinfo.pf.admin.service.UacResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
public class UacResourceServiceImpl implements UacResourceService {
    @Autowired
    private UacResourceMapper resourceMapper;
    @Autowired
    private UacAdminCacheService adminCacheService;
    @Override
    public int create(UacResource UacResource) {
        UacResource.setCreateTime(new Date());
        return resourceMapper.insert(UacResource);
    }

    @Override
    public int update(Long id, UacResource UacResource) {
        UacResource.setId(id);
        int count = resourceMapper.updateByPrimaryKeySelective(UacResource);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public UacResource getItem(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        int count = resourceMapper.deleteByPrimaryKey(id);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public List<UacResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UacResourceExample example = new UacResourceExample();
        UacResourceExample.Criteria criteria = example.createCriteria();
        if(categoryId!=null){
            criteria.andCategoryIdEqualTo(categoryId);
        }
        if(StrUtil.isNotEmpty(nameKeyword)){
            criteria.andNameLike('%'+nameKeyword+'%');
        }
        if(StrUtil.isNotEmpty(urlKeyword)){
            criteria.andUrlLike('%'+urlKeyword+'%');
        }
        return resourceMapper.selectByExample(example);
    }

    @Override
    public List<UacResource> listAll() {
        return resourceMapper.selectByExample(new UacResourceExample());
    }
}
