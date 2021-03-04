package com.financial.exchange.market.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	public void configure(HttpSecurity http) throws Exception {
	
		http.authorizeRequests().antMatchers(HttpMethod.GET,
				"/api/transactions",
				"/api/cambioCondiciones").permitAll()
//		.antMatchers(HttpMethod.GET, "/api/delegados/{id}").hasAnyRole("USER","ADMIN")
//		.antMatchers(HttpMethod.POST, "/api/delegados").hasRole("ADMIN")
//		.antMatchers("api/delegados/**").hasRole("ADMIN")
		.antMatchers("/api-docs/**").permitAll()
		.anyRequest().authenticated()
		.and().cors().configurationSource(corsConfigurationSource());
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration conf = new CorsConfiguration();
		conf.setAllowedOrigins(Arrays.asList("*"));
		conf.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		conf.setAllowCredentials(true);
		conf.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", conf);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
		
	}
}
