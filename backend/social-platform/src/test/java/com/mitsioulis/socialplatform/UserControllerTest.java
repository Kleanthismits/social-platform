package com.mitsioulis.socialplatform;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.mitsioulis.socialplatform.error.ApiError;
import com.mitsioulis.socialplatform.shared.GenericResponse;
import com.mitsioulis.socialplatform.user.User;
import com.mitsioulis.socialplatform.user.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class UserControllerTest {

	private static final String API_1_0_USERS = "/api/1.0/users";
	Logger                      log           = LoggerFactory.getLogger(UserControllerTest.class);

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	public void cleanUp() {

		userRepository.deleteAll();
	}

//	@ParameterizedTest
//	@CsvSource({ "1, Doe,1990-05-20, 1990-10-22, 1990-10-22, false" })
//	void testWithArgumentsAggregator(
//			@AggregateWith(MandatoryRestAfterShiftTestModelAggregator.class) MandatoryRestAfterShiftTestModel model) {
//		log.info(() -> model.toString());
//	}

//	@ParameterizedTest
//	@ValueSource(strings = { "01.01.2017", "31.12.2017" })
//	void testWithExplicitJavaTimeConverter(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {
//
//		assertThat(argument.getYear()).isEqualTo(2017);
//	}

//	@ParameterizedTest(name = "{index} Int: {0} String: {1} Boolean: {2} Date: {3}")
//	@CsvFileSource(resources = "/test.csv")
//	void test_Csv_File_Source(int i, String b, boolean c, LocalDate date) {
//		log.info(() -> i + " " + b + " " + c + " " + date);
//	}

//	@ParameterizedTest(name = "{index} Int: {0} String: {1} Boolean: {2} Date: {3}")
//	@CsvFileSource(resources = "/test.csv")
//	void test_Csv_File_Source_with_argument_Accessor(ArgumentsAccessor args) {
//		log.info(() -> args.getInteger(0) + " " + args.getString(1) + " " + args.getBoolean(2) + " "
//				+ args.get(3, LocalDate.class));
//	}

	@Test
	public void postUser_whenUserIsValid_receiveOk() {

		User user = DataProvider.createValidUser();
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void post_whenUserIsValid_userSavedToDatabase() {

		User user = DataProvider.createValidUser();
		postSignUp(user, Object.class);
		assertThat(userRepository.count()).isEqualTo(1);

	}

	@Test
	public void postUser_whenUserIsValid_receiveSuccessMessage() {

		User user = DataProvider.createValidUser();
		ResponseEntity<GenericResponse> response = postSignUp(user, GenericResponse.class);
		assertThat(response.getBody().getMessage()).isNotNull();
	}

	@Test
	public void postUser_whenUserIsValid_passwordIsHashedInDataBase() {

		User user = DataProvider.createValidUser();
		postSignUp(user, GenericResponse.class);
		List<User> users = userRepository.findAll();
		User dbUser = users.get(0);
		assertThat(dbUser.getPassword()).isNotEqualTo(user.getPassword());
	}

	@Test
	public void postUser_whenUserHasNullUserName_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		user.setUsername(null);
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasNullDisplayName_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		user.setDisplayName(null);
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasNullPassword_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		user.setPassword(null);
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasUserNameWithLessThanRequired_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		user.setUsername("abc");
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasDisplayWithLessThanRequired_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		user.setDisplayName("abc");
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasPasswordWithLessThanRequired_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		user.setPassword("P4sswor");
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasUserNameThatExceedsTheLengthLimit_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
		user.setUsername(valueOf256Chars);
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasDisplayNameThatExceedsTheLengthLimit_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
		user.setDisplayName(valueOf256Chars);
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserHasPasswordThatExceedsTheLengthLimit_receiveBadRequest() {

		User user = DataProvider.createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1, 256).mapToObj(x -> "a").collect(Collectors.joining());
		user.setPassword(valueOf256Chars + "A1");
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	// @Test
	// public void postUser_whenUserHasPasswordWithAllLowercase_receiveBadRequest()
	// {
	//
	// User user = UserControllerTestData.createValidUser();
	// user.setPassword("allowercase");
	// ResponseEntity<Object> response = postSignUp(user, Object.class);
	// assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	// }

	@ParameterizedTest(name = "#{index} - Run test with password = {0}")
	@MethodSource("validPasswordProvider")
	void postUser_whenUserHaValidPasswordReceiveOkRequest(String password) {

		User user = DataProvider.createValidUser();
		user.setPassword(password);
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@ParameterizedTest(name = "#{index} - Run test with password = {0}")
	@MethodSource("invalidPasswordProvider")
	void postUser_whenUserHasInValidPassword_receiveBadRequest(String password) {

		User user = DataProvider.createValidUser();
		user.setPassword(password);
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserIsInvalid_receiveBadRequest() {

		User user = new User();
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_USERS);
	}

	@Test
	public void postUser_whenUserIsInvalid_receiveApiErrorWithValidationErrors() {

		User user = new User();
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getValidationErrors().size()).isEqualTo(3);
	}

	@Test
	public void postUser_whenUserHasNullUserName_receiveMessageOfNullErrorForUsername() {

		User user = DataProvider.createValidUser();
		user.setUsername(null);
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getValidationErrors().get("username")).isEqualTo("Username must not be null");
	}

	@Test
	public void postUser_whenUserHasNullPassword_receiveMessageOfNullErrorForPassword() {

		User user = DataProvider.createValidUser();
		user.setPassword(null);
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getValidationErrors().get("password")).isEqualTo("Cannot not be null");
	}

	@Test
	public void postUser_whenUserHasInvalidLengthUserName_receiveMessageGenericMessageOfSizeError() {

		User user = DataProvider.createValidUser();
		user.setUsername("abc");
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getValidationErrors().get("username"))
				.isEqualTo("It must have minimum 4 and maximum 255 characters");
	}

	@Test
	public void postUser_whenUserHasInvalidPasswordPattern_receiveMessageOfPasswordPatternError() {

		User user = DataProvider.createValidUser();
		user.setPassword("ttttttttttt");
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getValidationErrors().get("password")).isEqualTo(
				"Password must have at least one uppercase, one lowercase, one number and one special character[!@#&()–[{}]:;',?/*~$^+=<>]");
	}

	@Test
	public void postUser_whenUserHasSameUserNameWithOtherUser_receiveBadRequest() {

		userRepository.save(DataProvider.createValidUser());
		User user = DataProvider.createValidUser();
		ResponseEntity<Object> response = postSignUp(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	public void postUser_whenUserHasSameUserNameWithOtherUser_receiveMessageOfDuplicateUsername() {

		userRepository.save(DataProvider.createValidUser());
		User user = DataProvider.createValidUser();
		ResponseEntity<ApiError> response = postSignUp(user, ApiError.class);
		assertThat(response.getBody().getValidationErrors().get("username")).isEqualTo("This name is in use");

	}

	static Stream<String> validPasswordProvider() {

		return Stream.of("AAAbbbccc@123", "Hello world$123", "A!@#&()–a1", // test punctuation part 1
				"A[{}]:;',?/*a1", // test punctuation part 2
				"A~$^+=<>a1", // test symbols
				"0123456789$abcdefgAB", // test 20 chars
				"123Aa$Aa" // test 8 chars
		);
	}

	// At least
	// one lowercase character,
	// one uppercase character,
	// one digit,
	// one special character
	// and length between 8 to 20.
	static Stream<String> invalidPasswordProvider() {

		return Stream.of("12345678", // invalid, only digit
				"abcdefgh", // invalid, only lowercase
				"ABCDEFGH", // invalid, only uppercase
				"abc123$$$", // invalid, at least one uppercase
				"ABC123$$$", // invalid, at least one lowercase
				"ABC$$$$$$", // invalid, at least one digit
				"java REGEX 123", // invalid, at least one special character
				"java REGEX 123 %", // invalid, % is not in the special character group []
				"________", // invalid
				"--------", // invalid
				" ", // empty
				""); // empty
	}

	public <T> ResponseEntity<T> postSignUp(Object request, Class<T> response) {

		return testRestTemplate.postForEntity(API_1_0_USERS, request, response);
	}
}
