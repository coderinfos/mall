package org.coderinfo.pf.mall.portal.service;

import org.coderinfo.pf.core.domain.uac.UacMember;

/**
 * 会员信息缓存业务类
 * Created by macro on 2020/3/14.
 */
public interface UacMemberCacheService {
    /**
     * 删除会员用户缓存
     */
    void delMember(Long memberId);

    /**
     * 获取会员用户缓存
     */
    UacMember getMember(String username);

    /**
     * 设置会员用户缓存
     */
    void setMember(UacMember member);

    /**
     * 设置验证码
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * 获取验证码
     */
    String getAuthCode(String telephone);
}
