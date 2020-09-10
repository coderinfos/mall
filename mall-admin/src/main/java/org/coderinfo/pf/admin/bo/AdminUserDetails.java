package org.coderinfo.pf.admin.bo;

import org.coderinfo.pf.core.domain.uac.UacAdmin;
import org.coderinfo.pf.core.domain.uac.UacResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 *
 * @author macro
 * @date 2018/4/26
 */
public class AdminUserDetails implements UserDetails {
    private UacAdmin UacAdmin;
    private List<UacResource> resourceList;
    public AdminUserDetails(UacAdmin UacAdmin,List<UacResource> resourceList) {
        this.UacAdmin = UacAdmin;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的角色
        return resourceList.stream()
                .map(role ->new SimpleGrantedAuthority(role.getId()+":"+role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return UacAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return UacAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UacAdmin.getStatus().equals(1);
    }
}
