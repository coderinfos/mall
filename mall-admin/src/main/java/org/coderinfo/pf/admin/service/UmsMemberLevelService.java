package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.core.domain.uac.UacMemberLevel;

import java.util.List;

/**
 * 会员等级管理Service
 * Created by macro on 2018/4/26.
 */
public interface UacMemberLevelService {
    /**
     * 获取所有会员登录
     * @param defaultStatus 是否为默认会员
     */
    List<UacMemberLevel> list(Integer defaultStatus);
}
