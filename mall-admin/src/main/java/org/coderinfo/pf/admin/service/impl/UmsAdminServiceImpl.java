package org.coderinfo.pf.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import org.coderinfo.pf.admin.bo.AdminUserDetails;
import org.coderinfo.pf.common.exception.Asserts;
import org.coderinfo.pf.admin.dao.UacAdminPermissionRelationDao;
import org.coderinfo.pf.admin.dao.UacAdminRoleRelationDao;
import org.coderinfo.pf.admin.dto.UacAdminParam;
import org.coderinfo.pf.admin.dto.UpdateAdminPasswordParam;
import org.coderinfo.pf.core.domain.uac.*;
import org.coderinfo.pf.core.mapper.UacAdminLoginLogMapper;
import org.coderinfo.pf.core.mapper.UacAdminMapper;
import org.coderinfo.pf.core.mapper.UacAdminPermissionRelationMapper;
import org.coderinfo.pf.core.mapper.UacAdminRoleRelationMapper;
import com.macro.mall.model.*;
import org.coderinfo.pf.mall.security.util.JwtTokenUtil;
import org.coderinfo.pf.admin.service.UacAdminCacheService;
import org.coderinfo.pf.admin.service.UacAdminService;
import org.coderinfo.pf.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UacAdminService实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UacAdminServiceImpl implements UacAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UacAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UacAdminMapper adminMapper;
    @Autowired
    private UacAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UacAdminRoleRelationDao adminRoleRelationDao;
    @Autowired
    private UacAdminPermissionRelationMapper adminPermissionRelationMapper;
    @Autowired
    private UacAdminPermissionRelationDao adminPermissionRelationDao;
    @Autowired
    private UacAdminLoginLogMapper loginLogMapper;
    @Autowired
    private UacAdminCacheService adminCacheService;

    @Override
    public UacAdmin getAdminByUsername(String username) {
        UacAdmin admin = adminCacheService.getAdmin(username);
        if(admin!=null) return  admin;
        UacAdminExample example = new UacAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UacAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            adminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UacAdmin register(UacAdminParam UacAdminParam) {
        UacAdmin UacAdmin = new UacAdmin();
        BeanUtils.copyProperties(UacAdminParam, UacAdmin);
        UacAdmin.setCreateTime(new Date());
        UacAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        UacAdminExample example = new UacAdminExample();
        example.createCriteria().andUsernameEqualTo(UacAdmin.getUsername());
        List<UacAdmin> UacAdminList = adminMapper.selectByExample(example);
        if (UacAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(UacAdmin.getPassword());
        UacAdmin.setPassword(encodePassword);
        adminMapper.insert(UacAdmin);
        return UacAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("密码不正确");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
//            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        UacAdmin admin = getAdminByUsername(username);
        if(admin==null) return;
        UacAdminLoginLog loginLog = new UacAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }

    /**
     * 根据用户名修改登录时间
     */
    private void updateLoginTimeByUsername(String username) {
        UacAdmin record = new UacAdmin();
        record.setLoginTime(new Date());
        UacAdminExample example = new UacAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        adminMapper.updateByExampleSelective(record, example);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public UacAdmin getItem(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UacAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UacAdminExample example = new UacAdminExample();
        UacAdminExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andUsernameLike("%" + keyword + "%");
            example.or(example.createCriteria().andNickNameLike("%" + keyword + "%"));
        }
        return adminMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UacAdmin admin) {
        admin.setId(id);
        UacAdmin rawAdmin = adminMapper.selectByPrimaryKey(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        int count = adminMapper.updateByPrimaryKeySelective(admin);
        adminCacheService.delAdmin(id);
        return count;
    }

    @Override
    public int delete(Long id) {
        adminCacheService.delAdmin(id);
        int count = adminMapper.deleteByPrimaryKey(id);
        adminCacheService.delResourceList(id);
        return count;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        UacAdminRoleRelationExample adminRoleRelationExample = new UacAdminRoleRelationExample();
        adminRoleRelationExample.createCriteria().andAdminIdEqualTo(adminId);
        adminRoleRelationMapper.deleteByExample(adminRoleRelationExample);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UacAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UacAdminRoleRelation roleRelation = new UacAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationDao.insertList(list);
        }
        adminCacheService.delResourceList(adminId);
        return count;
    }

    @Override
    public List<UacRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public List<UacResource> getResourceList(Long adminId) {
        List<UacResource> resourceList = adminCacheService.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            return  resourceList;
        }
        resourceList = adminRoleRelationDao.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            adminCacheService.setResourceList(adminId,resourceList);
        }
        return resourceList;
    }

    @Override
    public int updatePermission(Long adminId, List<Long> permissionIds) {
        //删除原所有权限关系
        UacAdminPermissionRelationExample relationExample = new UacAdminPermissionRelationExample();
        relationExample.createCriteria().andAdminIdEqualTo(adminId);
        adminPermissionRelationMapper.deleteByExample(relationExample);
        //获取用户所有角色权限
        List<UacPermission> permissionList = adminRoleRelationDao.getRolePermissionList(adminId);
        List<Long> rolePermissionList = permissionList.stream().map(UacPermission::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<UacAdminPermissionRelation> relationList = new ArrayList<>();
            //筛选出+权限
            List<Long> addPermissionIdList = permissionIds.stream().filter(permissionId -> !rolePermissionList.contains(permissionId)).collect(Collectors.toList());
            //筛选出-权限
            List<Long> subPermissionIdList = rolePermissionList.stream().filter(permissionId -> !permissionIds.contains(permissionId)).collect(Collectors.toList());
            //插入+-权限关系
            relationList.addAll(convert(adminId,1,addPermissionIdList));
            relationList.addAll(convert(adminId,-1,subPermissionIdList));
            return adminPermissionRelationDao.insertList(relationList);
        }
        return 0;
    }

    /**
     * 将+-权限关系转化为对象
     */
    private List<UacAdminPermissionRelation> convert(Long adminId,Integer type,List<Long> permissionIdList) {
        List<UacAdminPermissionRelation> relationList = permissionIdList.stream().map(permissionId -> {
            UacAdminPermissionRelation relation = new UacAdminPermissionRelation();
            relation.setAdminId(adminId);
            relation.setType(type);
            relation.setPermissionId(permissionId);
            return relation;
        }).collect(Collectors.toList());
        return relationList;
    }

    @Override
    public List<UacPermission> getPermissionList(Long adminId) {
        return adminRoleRelationDao.getPermissionList(adminId);
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        UacAdminExample example = new UacAdminExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<UacAdmin> adminList = adminMapper.selectByExample(example);
        if(CollUtil.isEmpty(adminList)){
            return -2;
        }
        UacAdmin UacAdmin = adminList.get(0);
        if(!passwordEncoder.matches(param.getOldPassword(),UacAdmin.getPassword())){
            return -3;
        }
        UacAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        adminMapper.updateByPrimaryKey(UacAdmin);
        adminCacheService.delAdmin(UacAdmin.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        //获取用户信息
        UacAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UacResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
