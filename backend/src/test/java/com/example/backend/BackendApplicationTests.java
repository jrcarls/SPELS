package com.example.backend;

import com.example.backend.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void registersAndUsesTokenToReadCurrentUser() throws Exception {
		String response = mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Jean\",\"email\":\"jean@example.com\",\"password\":\"password123\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accessToken").isNotEmpty())
				.andReturn().getResponse().getContentAsString();

		String token = response.replaceFirst(".*\\\"accessToken\\\":\\\"([^\\\"]+)\\\".*", "$1");

		mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("jean@example.com"))
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.password").doesNotExist());

		var user = userRepository.findByEmail("jean@example.com").orElseThrow();
		user.setActive(false);
		userRepository.save(user);

		mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + token))
				.andExpect(status().isUnauthorized());
	}

}
