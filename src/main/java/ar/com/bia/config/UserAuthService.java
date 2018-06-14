package ar.com.bia.config;

import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.entity.UserDoc;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;

//http://thinkinginsoftware.blogspot.com.ar/2011/07/redirect-after-login-to-requested-page.html

public class UserAuthService implements UserDetailsService {

	private UserRepositoryImpl userRepository;
	
	
	public UserAuthService(UserRepositoryImpl userRepository) {
		super();
		this.userRepository = userRepository;
	}



	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {	
		boolean enabled = true;
		boolean userNotExpired = true;
		boolean credentialsNotExpired = true;
		boolean locked = true;
		
		UserDoc user = userRepository.findUser(username);
		if(user == null)
			throw new UsernameNotFoundException(username);
//		if(user.getPassword().equals(anObject) == null)
//			throw new BadCredentialsException(username);
		UserDetails userDetails = new User(username, user.getPassword(),
				enabled, userNotExpired,credentialsNotExpired, locked,new HashSet<GrantedAuthority>());
		
		return userDetails;
	}

}
