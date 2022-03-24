package br.com.letscode.movies.batlle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.letscode.movies.batlle.presenter.rest.controllers.MovieBattleController;
import br.com.letscode.movies.batlle.presenter.rest.dtos.request.LoginRequest;
import br.com.letscode.movies.batlle.presenter.rest.dtos.request.QuizzGuessRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class MoviesBatlleApplicationTests {

	@Autowired
	MovieBattleController moviesBattleController;

	@Autowired
	MockMvc mockMvc;

	private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHQUJSSUVMIiwiaWF0IjoxNjQ3OTk5MzE1LCJleHAiOjE2NDg5OTkzMTV9.rKb9Aiev0LLaYKr617DxU5hkAYFYHQLle-T3X77P73zRnaWBPQzITQPhipGKiXhxloMzrH3_-yIx6vkG3UKDKw";

	@Test
	void contextLoads() {
		assertTrue(true);
	}

	@Test
	@Order(1)
	void shouldReturn200WithAccessTokenWhenUserIsAuthorized() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		var signIn = new LoginRequest("GABRIEL", "123456");
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson= ow.writeValueAsString(signIn);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.post("http://localhost:8080/api/auth/signin")
			.content(requestJson)
			.contentType(MediaType.APPLICATION_JSON);
		
		var result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Before All init() method called");
		MockHttpServletResponse response = result.getResponse();
		System.out.println(result.getResponse());
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(response.getStatus(), 200);
	}

	@Test
	void shouldReturn401WhenUserIsUnauthorized() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		var signIn = new LoginRequest("GABRIEL", "1234567");
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson= ow.writeValueAsString(signIn);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.post("http://localhost:8080/api/auth/signin")
			.content(requestJson)
			.contentType(MediaType.APPLICATION_JSON);
		
		var result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Before All init() method called");
		MockHttpServletResponse response = result.getResponse();
		System.out.println(result.getResponse());
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(response.getStatus(), 401);
	}

	@Test
	@Order(2)
	void shouldCreateNewMoviesBatlleAndReturn201() throws Exception {
		Principal mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("GABRIEL");
		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.post("http://localhost:8080/api/movies_battle/begin")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.principal(mockPrincipal)
			.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();
		assertEquals(201, status);
	}

	@Test
	@Order(3)
	void shouldReturnAResposeWithANewFilmPair() throws Exception {
		Principal mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("GABRIEL");
		MockHttpServletResponse response = getNextRandomFilms();
		int status = response.getStatus();
		System.out.println(status);
	}

	@Test
	void shouldReturnBadRequestWhenPlayerGuessWrongFilm() throws Exception {
		// GIVEN
		ObjectMapper mapper = new ObjectMapper();
		var QuizzGuessRequest = new QuizzGuessRequest("9999", "9998");
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(QuizzGuessRequest);

		// WHEN

		Principal mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("GABRIEL");
		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.post("http://localhost:8080/api/movies_battle/quizz")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.content(requestJson)
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		// THEN
		assertEquals(status, 400);
	}

	// @Test
	// void shouldReturnStatusOKWhenPlayerGuessRightFilm() throws Exception {
	// 	// GIVEN
	// 	ObjectMapper mapper = new ObjectMapper();
	// 	MockHttpServletResponse randomFilmsResponse = getNextRandomFilms();
	// 	String contentAsString = randomFilmsResponse.getContentAsString();
	// 	List<Film> films = mapper.readValue(contentAsString, new TypeReference<List<Film>>(){});

	// 	var QuizzGuessRequest = new QuizzGuessRequest(films.get(0).getId(), films.get(1).getId());
	// 	mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	// 	ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	// 	String requestJson = ow.writeValueAsString(QuizzGuessRequest);

	// 	Principal mockPrincipal = Mockito.mock(Principal.class);
	// 	Mockito.when(mockPrincipal.getName()).thenReturn("GABRIEL");
		
	// 	// WHEN
	// 	RequestBuilder requestBuilder = MockMvcRequestBuilders
	// 		.post("http://localhost:8080/api/movies_battle/quizz")
	// 		.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
	// 		.content(requestJson)
	// 		.contentType(MediaType.APPLICATION_JSON);

	// 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	// 	MockHttpServletResponse response = result.getResponse();
	// 	int status = response.getStatus();

	// 	System.out.println("statusstatusstatusstatus");
	// 	System.out.println(status);
	// 	// THEN
	// 	assertEquals(status, 200);
	// }

	private MockHttpServletResponse getNextRandomFilms() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.get("http://localhost:8080/api/movies_battle/quizz")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		return response;
	}
}
