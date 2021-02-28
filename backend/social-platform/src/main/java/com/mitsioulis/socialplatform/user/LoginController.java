package com.mitsioulis.socialplatform.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.mitsioulis.socialplatform.shared.annotations.CurrentUser;

@RestController
public class LoginController {

	@PostMapping("/api/1.0/login")
	@JsonView(Views.Base.class)
	User handleLogin(@CurrentUser User loggedInUser) {
		return loggedInUser;
	}

	// alternative ways
//	@PostMapping("/api/1.0/login")
//	Map<String, Object> handleLogin() {
//		User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return Collections.singletonMap("id", loggedInUser.getId());
//	}
//
//	@PostMapping("/api/1.0/login")
//	Map<String, Object> handleLogin(Authentication authentication) {
//		User loggedInUser = (User) authentication.getPrincipal();
//		return Collections.singletonMap("id", loggedInUser.getId());
//	}
}
