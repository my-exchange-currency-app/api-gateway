package com.demo.skyros.vo;


import com.demo.skyros.vo.enums.LoginStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Setter
@Getter
public class AppUserDetails implements UserDetails {

    private Long id;
    private String userName;
    private String password;
    private Set<RoleVO> roles = new HashSet<>();
    private LoginStatusEnum loginStatusEnum;
    private boolean isEnabled;
    private boolean isCredentialsNonExpired;
    private boolean isAccountNonLocked;
    private boolean isAccountNonExpired;

    public AppUserDetails(AppUserVO appUserVO) {
        this.id = appUserVO.getId();
        this.userName = appUserVO.getEmail();
        this.password = appUserVO.getPassword();
        this.loginStatusEnum = appUserVO.getLoginStatusEnum();
        this.roles = appUserVO.getRoles();
        this.isEnabled = appUserVO.isEnabled();
        this.isCredentialsNonExpired = appUserVO.isCredentialsNonExpired();
        this.isAccountNonLocked = appUserVO.isAccountNonLocked();
        this.isAccountNonExpired = appUserVO.isAccountNonExpired();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getUserGrantedAuthority(roles);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    private List<GrantedAuthority> getUserGrantedAuthority(Set<RoleVO> roles) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        roles.forEach(roleVO -> {
            grantedAuthorityList.add(new SimpleGrantedAuthority(roleVO.getCode()));
        });
        return grantedAuthorityList;
    }
}
