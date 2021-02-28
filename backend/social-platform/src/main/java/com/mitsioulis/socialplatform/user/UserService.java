//$Author$
//$Date$
//$Revision$
//
//
//Original Author: Yanis Charkiolakis
//Created on: Feb 6, 2021 6:14:19 PM
//-----------------------------------------------------------------------------
package com.mitsioulis.socialplatform.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author kleanthis mitsioulis
 *
 */
@Service
public class UserService {

	UserRepository  userRepository;
	PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		super();
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	public User saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
