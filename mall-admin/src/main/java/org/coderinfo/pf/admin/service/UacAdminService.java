package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.admin.dto.UacAdminParam;
import org.coderinfo.pf.admin.dto.UpdateAdminPasswordParam;
import org.coderinfo.pf.core.domain.uac.UacAdmin;
import org.coderinfo.pf.core.domain.uac.UacPermission;
import org.coderinfo.pf.core.domain.uac.UacResource;
import org.coderinfo.pf.core.domain.uac.UacRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台管理员Service
 * Created by macro on 2018/4/26.
 */
public interface UacAdminService {
    /**
     * 根据用户名获取后台管理员
     */
    UacAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    UacAdmin register(UacAdminParam UacAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username,String password);

    /**
     * 刷新token的功能
     * @param oldToken 旧的token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户id获取用户
     */
    UacAdmin getItem(Long id);

    /**
     * 根据用户名或昵称分页查询用户
     */
    List<UacAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    int update(Long id, UacAdmin admin);

    /**
     * 删除指定用户
     */
    int delete(Long id);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对于角色
     */
    List<UacRole> getRoleList(Long adminId);

    /**
     * 获取指定用户的可访问资源
     */
    List<UacResource> getResourceList(Long adminId);

    /**
     * 修改用户的+-权限
     */
    @Transactional
    int updatePermission(Long adminId, List<Long> permissionIds);

    /**
     * 获取用户所有权限（包括角色权限和+-权限）
     */
    List<UacPermission> getPermissionList(Long adminId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);
}
