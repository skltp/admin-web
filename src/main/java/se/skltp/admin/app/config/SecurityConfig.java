/**
 * Copyright (c) 2014 Center for eHalsa i samverkan (CeHis).
 * 								<http://cehis.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.admin.app.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile("default")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.passwordEncoder(passwordEncoder())
			.usersByUsernameQuery("select anvandarnamn AS username, losenord_hash AS password, true AS enabled from Anvandare where anvandarnamn = ?")
			.authoritiesByUsernameQuery("select anvandarnamn AS username, CASE WHEN administrator = 1 THEN 'ROLE_ADMIN' ELSE 'ROLE_USER' END AS authority from Anvandare where anvandarnamn = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/**").hasAnyRole("ADMIN", "USER")
			.anyRequest().anonymous().and().httpBasic();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new MessageDigestPasswordEncoder("SHA-1");
	}
}
