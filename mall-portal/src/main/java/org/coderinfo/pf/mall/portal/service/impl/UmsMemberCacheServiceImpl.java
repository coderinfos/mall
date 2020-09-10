package org.coderinfo.pf.mall.portal.service.impl;

import org.coderinfo.pf.common.service.RedisService;
import org.coderinfo.pf.core.mapper.UacMemberMapper;
import org.coderinfo.pf.core.domain.uac.UacMember;
import org.coderinfo.pf.mall.portal.service.UacMemberCacheService;
import org.coderinfo.pf.mall.security.annotation.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * UacMemberCacheService实现类
 * Created by macro on 2020/3/14.
 */
@Service
public class UacMemberCacheServiceImpl implements UacMemberCacheService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UacMemberMapper memberMapper;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE;
    @Value("${redis.key.member}")
    private String REDIS_KEY_MEMBER;
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE;

    @Override
    public void delMember(Long memberId) {
        UacMember UacMember = memberMapper.selectByPrimaryKey(memberId);
        if (UacMember != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + UacMember.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public UacMember getMember(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + username;
        return (UacMember) redisService.get(key);
    }

    @Override
    public void setMember(UacMember member) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + member.getUsername();
        redisService.set(key, member, REDIS_EXPIRE);
    }

    @CacheException
    @Override
    public void setAuthCode(String telephone, String authCode) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        redisService.set(key,authCode,REDIS_EXPIRE_AUTH_CODE);
    }

    @CacheException
    @Override
    public String getAuthCode(String telephone) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        return (String) redisService.get(key);
    }
}
