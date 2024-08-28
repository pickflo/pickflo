package com.pickflo.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Entity
@Table(name = "USERS")
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NaturalId
	@Basic(optional = false)
	private String email;
	
	@Basic(optional = false)
	private String password;
	
	@Basic(optional = false)
	private String nickname;

	@Basic(optional = false)
	private LocalDate birth;
	
	@Basic(optional = false)
	private int gender;   //0:남성 1:여성
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getUsername() {
		
		return this.email;
	}

	

}
