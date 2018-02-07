package ar.com.bia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import ar.com.bia.backend.dao.impl.UserRepositoryImpl;

@Configuration
@ComponentScan(basePackages = { "ar.com.bia.config", "ar.com.bia.backend.dao.impl" })
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepositoryImpl userRepository;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
		// auth.inMemoryAuthentication().withUser("user").password("password")
		// .roles("USER");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new AuthenticationSuccessHandler();
	}

	protected void configure(HttpSecurity http) throws Exception {
		// http
		// .authorizeRequests()
		// .anyRequest().authenticated()
		// .and()
		// .formLogin()
		// .loginPage("/login")
		// .permitAll();
		// http.authorizeRequests()
		// .antMatchers("/resources/**", "/signup", "/about").permitAll().
		// anyRequest().authenticated().and().formLogin().and().httpBasic();

		http
				// .csrf().disable()
				.csrf().disable().headers().frameOptions().disable()
				// .authorizeRequests()
				// .antMatchers("*/xomeq/login*")
				// .anonymous()
				// .and()
				.authorizeRequests().antMatchers("/public/**").permitAll().antMatchers("/login*").permitAll() // Para
																												// poder
																												// cambiar
																												// el
																												// lenguaje
																												// en
																												// el
																												// login
				// .antMatchers("/**").authenticated()
				.and().formLogin().loginPage("/login").failureUrl("/login?error=true").successHandler(successHandler())
				// .loginProcessingUrl("/rest/login")
				// .defaultSuccessUrl("/genome",true)
				.permitAll()

				// .failureHandler(authenticationFailureHandler)
				.and().logout()
				// .logoutSuccessUrl("/")
				.permitAll();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserAuthService(userRepository);
	}

}
