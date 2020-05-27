package com.example.homework.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.csrf().disable() //csrf 취약점 보안을 해제 on으로 하면 웹페이지에서 추가 처리 해야함.
			 
			.headers()
				.frameOptions().sameOrigin()
				// SockJS는 기본적으로 HTML iframe 요소를 통한 정송을 허용하지 않도록 설정되어 있으므로 해제함.
				
			.and()
				.formLogin() // 권한없이 로그인하면 로그인 페이지로 이동.
			.and()
				.authorizeRequests()
					.antMatchers("/chat/**").hasRole("USER") //chat으로 시작하는 리소스들은 접근권한이 있어야함
					.anyRequest().permitAll(); // 나머지 리소스들은 그냥 들어올수 있게 접근 설정
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication()
				.withUser("wan")
				.password("{noop}123")
				.roles("USER")
			.and()
				.withUser("wan2")
				.password("{noop}123")
				.roles("USER")
			.and()
				.withUser("guest")
				.password("{noop}123")
				.roles("GUEST");
	}

}
