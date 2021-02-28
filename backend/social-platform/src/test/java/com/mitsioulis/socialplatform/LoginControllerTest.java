package com.mitsioulis.socialplatform;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;

import com.mitsioulis.socialplatform.error.ApiError;
import com.mitsioulis.socialplatform.user.User;
import com.mitsioulis.socialplatform.user.UserRepository;
import com.mitsioulis.socialplatform.user.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class LoginControllerTest {

	private static final String API_1_0_LOGIN = "/api/1.0/login";
	@Autowired
	TestRestTemplate            testRestTemplate;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@BeforeEach
	public void cleanUp() {

		userRepository.deleteAll();
		testRestTemplate.getRestTemplate().getInterceptors().clear();
	}

	@Test
	public void postLogin_withoutUserCredentials_receiveUnauthorized() {
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void postLogin_withIncorrectCredentials_receiveUnauthorized() {
		authenticate();
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void postLogin_withoutUserCredentials_receiveApiError() {
		ResponseEntity<ApiError> response = login(ApiError.class);
		assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_LOGIN);
	}

	@Test
	public void postLogin_withoutUserCredentials_receiveApiErrorWithoutValidationErrors() {
		ResponseEntity<String> response = login(String.class);
		assertThat(response.getBody().contains("validationErrors")).isFalse();
	}

	@Test
	public void postLogin_withoutUserCredentials_receiveUnauthorizedWithoutWWWAuhtenticationHeader() {
		authenticate();
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getHeaders().containsKey("WWW-Authenticate")).isFalse();
	}

	@Test
	public void postLoginWithValidCredentials_receiveOk() {
		userService.saveUser(DataProvider.createValidUser());
		authenticate();
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void postLoginWithValidCredentials_receiveLoggedInUserId() {
		User inDB = userService.saveUser(DataProvider.createValidUser());
		authenticate();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String, Object>>() {
		});
		Map<String, Object> body = response.getBody();
		Integer id = (Integer) body.get("id");
		assertThat(id).isEqualTo(inDB.getId());
	}

	@Test
	public void postLoginWithValidCredentials_receiveLoggedInUserImage() {
		User inDB = userService.saveUser(DataProvider.createValidUser());
		authenticate();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String, Object>>() {
		});
		Map<String, Object> body = response.getBody();
		String image = (String) body.get("image");
		assertThat(image).isEqualTo(inDB.getImage());
	}

	@Test
	public void postLoginWithValidCredentials_receiveLoggedInUserDisplayName() {
		User inDB = userService.saveUser(DataProvider.createValidUser());
		authenticate();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String, Object>>() {
		});
		Map<String, Object> body = response.getBody();
		String displayName = (String) body.get("displayName");
		assertThat(displayName).isEqualTo(inDB.getDisplayName());
	}

	@Test
	public void postLoginWithValidCredentials_receiveLoggedInUserUsername() {
		User inDB = userService.saveUser(DataProvider.createValidUser());
		authenticate();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String, Object>>() {
		});
		Map<String, Object> body = response.getBody();
		String username = (String) body.get("username");
		assertThat(username).isEqualTo(inDB.getUsername());
	}

	@Test
	public void postLoginWithValidCredentials_notReceiveLoggedInUserPassword() {
		userService.saveUser(DataProvider.createValidUser());
		authenticate();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String, Object>>() {
		});
		Map<String, Object> body = response.getBody();
		assertThat(body.containsKey("password")).isFalse();
	}

	private void authenticate() {
		testRestTemplate.getRestTemplate().getInterceptors()
				.add(new BasicAuthenticationInterceptor("test-user", "P@Ssw0rd"));
	}

	public <T> ResponseEntity<T> login(Class<T> response) {

		return testRestTemplate.postForEntity(API_1_0_LOGIN, null, response);
	}

	public <T> ResponseEntity<T> login(ParameterizedTypeReference<T> responseType) {

		return testRestTemplate.exchange(API_1_0_LOGIN, HttpMethod.POST, null, responseType);
	}

}