package org.coderinfo.pf.admin.service.impl;

import org.coderinfo.pf.core.mapper.UacMemberLevelMapper;
import org.coderinfo.pf.core.domain.uac.UacMemberLevel;
import org.coderinfo.pf.admin.service.UacMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员等级管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UacMemberLevelServiceImpl implements UacMemberLevelService{
    @Autowired
    private UacMemberLevelMapper memberLevelMapper;
    @Override
    public List<UacMemberLevel> list(Integer defaultStatus) {
        UacMemberLevelExample example = new UacMemberLevelExample();
        example.createCriteria().andDefaultStatusEqualTo(defaultStatus);
        return memberLevelMapper.selectByExample(example);
    }
}
