package com.ztdx.eams.controller;

import com.ztdx.eams.query.SystemSecurityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SystemSecurityQuery systemSecurityQuery;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Set<String> list = systemSecurityQuery.getUserPermissions(userName);
        //权限设置
        Collection<GrantedAuthority> grantedAuthorities = this.getGrantedAuthorities(list);

        Map<String, Object> map = systemSecurityQuery.getUser(userName);
        String pwd = (String)map.get("pwd");
        Integer flag = (Integer)map.get("flag");
        User securityUser = new User(userName, pwd,
                flag.equals(0), true, true, true, grantedAuthorities);
        return securityUser;
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Set<String> list) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (String permission: list) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission));
        }
        return grantedAuthorities;
    }
}
