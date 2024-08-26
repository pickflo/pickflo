package com.pickflo.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pickflo.domain.User;
import com.pickflo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncode;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("email={}",email);
		Optional<User> entity = userRepo.findByEmail(email);
		if(entity.isPresent()) {
			return entity.get();
		} else {
			throw new UsernameNotFoundException(email + ": 일치하는 이메일이 없습니다");
		}
	}
	
    @Transactional
    public User create(User user) {
        log.info("create(member={})", user);
        user.setPassword(passwordEncode.encode(user.getPassword()));
        return userRepo.save(user);
    }
    
    @Transactional
    public User findByEmail (String email) {     
        Optional<User> memberOptional = userRepo.findByEmail(email);
        return memberOptional.orElse(null);
    }
    
    public Boolean checkPassword(String password) {
    	Optional<User> member = userRepo.findByPassword(password);
    	if(!member.isPresent()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public Boolean checkEmail(String email) {
    	Optional<User> member = userRepo.findByEmail(email);
    	if(!member.isPresent()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public Boolean checkNickname(String nickname) {
    	Optional<User> member = userRepo.findByNickname(nickname);
    	if(!member.isPresent()) {
    		return true;
    	} else {
    		return false;
    	}
    }


}
