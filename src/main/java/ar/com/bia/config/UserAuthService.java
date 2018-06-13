package ar.com.bia.config;

import java.util.HashSet;

import ar.com.bia.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.entity.UserDoc;
import org.springframework.stereotype.Component;

//http://thinkinginsoftware.blogspot.com.ar/2011/07/redirect-after-login-to-requested-page.html


public class UserAuthService implements UserDetailsService {


    private UserService userService;


    public UserAuthService(UserService userService) {
        super();
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        boolean enabled = true;
        boolean userNotExpired = true;
        boolean credentialsNotExpired = true;
        boolean locked = true;

        UserDoc user = userService.findUser(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
//		if(user.getPassword().equals(anObject) == null)
//			throw new BadCredentialsException(username);
        UserDetails userDetails = new User(username, user.getPassword(),
                enabled, userNotExpired, credentialsNotExpired, locked, new HashSet<GrantedAuthority>());

        return userDetails;
    }

}
