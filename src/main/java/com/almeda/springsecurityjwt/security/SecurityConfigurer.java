package com.almeda.springsecurityjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.almeda.springsecurityjwt.security.filters.JwtRequestFilter;
import com.almeda.springsecurityjwt.security.services.MyUserDetailsService;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(myUserDetailsService);
	}
	
	/* Sin implementar ningun filtro de seguridad
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable()
				   .authorizeRequests()
				   .antMatchers("/api/v1/saludo/authenticate", "/api/v1/saludo/hello")
				   .permitAll()
				   .anyRequest()
				   .authenticated();
	}
	*/
	
	/*
	 * Se modifica el metodo configure porque se implemento el filtro de seguridad
	 * JwtRequestFilter
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable()
				   .authorizeRequests()
				   .antMatchers("/api/v1/saludo/authenticate")
				   .permitAll()
				   .anyRequest()
				   .authenticated()
				   .and()
				   .sessionManagement()
				   .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
