package org.coderinfo.pf.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.coderinfo.pf.common.service.RedisService;
import org.coderinfo.pf.admin.dao.UacAdminRoleRelationDao;
import org.coderinfo.pf.core.mapper.UacAdminRoleRelationMapper;
import org.coderinfo.pf.core.domain.uac.UacAdmin;
import org.coderinfo.pf.core.domain.uac.UacAdminRoleRelation;
import org.coderinfo.pf.core.domain.uac.UacResource;
import org.coderinfo.pf.admin.service.UacAdminCacheService;
import org.coderinfo.pf.admin.service.UacAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UacAdminCacheService实现类
 * Created by macro on 2020/3/13.
 */
@Service
public class UacAdminCacheServiceImpl implements UacAdminCacheService {
    @Autowired
    private UacAdminService adminService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UacAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UacAdminRoleRelationDao adminRoleRelationDao;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public void delAdmin(Long adminId) {
        UacAdmin admin = adminService.getItem(adminId);
        if (admin != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public void delResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        UacAdminRoleRelationExample example = new UacAdminRoleRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<UacAdminRoleRelation> relationList = adminRoleRelationMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(relationList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        UacAdminRoleRelationExample example = new UacAdminRoleRelationExample();
        example.createCriteria().andRoleIdIn(roleIds);
        List<UacAdminRoleRelation> relationList = adminRoleRelationMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(relationList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIdList = adminRoleRelationDao.getAdminIdList(resourceId);
        if (CollUtil.isNotEmpty(adminIdList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = adminIdList.stream().map(adminId -> keyPrefix + adminId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public UacAdmin getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (UacAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(UacAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
        redisService.set(key, admin, REDIS_EXPIRE);
    }

    @Override
    public List<UacResource> getResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        return (List<UacResource>) redisService.get(key);
    }

    @Override
    public void setResourceList(Long adminId, List<UacResource> resourceList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.set(key, resourceList, REDIS_EXPIRE);
    }
}
