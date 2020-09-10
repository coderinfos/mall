package org.coderinfo.pf.mall.portal.domain;

import org.coderinfo.pf.core.domain.uac.UacMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

/**
 * 会员详情封装
 * Created by macro on 2018/8/3.
 */
public class MemberDetails implements UserDetails {
    private UacMember UacMember;

    public MemberDetails(UacMember UacMember) {
        this.UacMember = UacMember;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        return Arrays.asList(new SimpleGrantedAuthority("TEST"));
    }

    @Override
    public String getPassword() {
        return UacMember.getPassword();
    }

    @Override
    public String getUsername() {
        return UacMember.getUsername();
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
        return UacMember.getStatus()==1;
    }

    public UacMember getUacMember() {
        return UacMember;
    }
}
