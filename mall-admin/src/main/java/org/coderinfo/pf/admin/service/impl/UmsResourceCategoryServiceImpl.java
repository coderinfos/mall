package org.coderinfo.pf.admin.service.impl;

import org.coderinfo.pf.core.mapper.UacResourceCategoryMapper;
import org.coderinfo.pf.core.domain.uac.UacResourceCategory;
import org.coderinfo.pf.admin.service.UacResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源分类管理Service实现类
 * Created by macro on 2020/2/5.
 */
@Service
public class UacResourceCategoryServiceImpl implements UacResourceCategoryService {
    @Autowired
    private UacResourceCategoryMapper resourceCategoryMapper;

    @Override
    public List<UacResourceCategory> listAll() {
        UacResourceCategoryExample example = new UacResourceCategoryExample();
        example.setOrderByClause("sort desc");
        return resourceCategoryMapper.selectByExample(example);
    }

    @Override
    public int create(UacResourceCategory UacResourceCategory) {
        UacResourceCategory.setCreateTime(new Date());
        return resourceCategoryMapper.insert(UacResourceCategory);
    }

    @Override
    public int update(Long id, UacResourceCategory UacResourceCategory) {
        UacResourceCategory.setId(id);
        return resourceCategoryMapper.updateByPrimaryKeySelective(UacResourceCategory);
    }

    @Override
    public int delete(Long id) {
        return resourceCategoryMapper.deleteByPrimaryKey(id);
    }
}
