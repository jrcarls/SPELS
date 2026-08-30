package com.example.backend;

import com.example.backend.users.UserRepository;
import com.example.backend.organizations.OrganizationMemberRepository;
import com.example.backend.organizations.OrganizationRepository;
import com.example.backend.organizations.TenantRole;
import com.example.backend.subscriptions.SubscriptionRepository;
import com.example.backend.subscriptions.SubscriptionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class BackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrganizationMemberRepository organizationMemberRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void registersAndUsesTokenToReadCurrentUser() throws Exception {
		String response = mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Jean\",\"organizationName\":\"Confeitaria Jean\",\"cnpj\":\"12.345.678/0001-99\",\"email\":\"jean@example.com\",\"password\":\"password123\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accessToken").isNotEmpty())
				.andExpect(jsonPath("$.organizationId").isString())
				.andReturn().getResponse().getContentAsString();

		String token = response.replaceFirst(".*\\\"accessToken\\\":\\\"([^\\\"]+)\\\".*", "$1");

		mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("jean@example.com"))
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.password").doesNotExist());

		var user = userRepository.findByEmail("jean@example.com").orElseThrow();
		var membership = organizationMemberRepository.findFirstByUserIdAndActiveTrue(user.getId()).orElseThrow();
		assertEquals(TenantRole.OWNER, membership.getRole());
		assertEquals("confeitaria-jean", membership.getOrganization().getSlug());
		assertEquals("12345678000199", membership.getOrganization().getCnpj());
		assertEquals(SubscriptionStatus.TRIAL, subscriptionRepository
				.findByOrganizationId(membership.getOrganization().getId()).orElseThrow().getStatus());

		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Maria\",\"organizationName\":\"Confeitaria Jean\",\"cnpj\":\"00.000.000/E08G-12\",\"email\":\"maria@example.com\",\"password\":\"password123\"}"))
				.andExpect(status().isCreated());
		var secondOrganization = organizationRepository.findBySlug("confeitaria-jean-2").orElseThrow();
		assertEquals("Confeitaria Jean", secondOrganization.getName());
		assertEquals("00000000E08G12", secondOrganization.getCnpj());

		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Ana\",\"organizationName\":\"Confeitaria Ana\",\"cnpj\":\"12345678000199\",\"email\":\"ana@example.com\",\"password\":\"password123\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("CNPJ já cadastrado"));

		user.setPlatformRole(com.example.backend.users.PlatformRole.ADMIN);
		userRepository.save(user);

		mockMvc.perform(patch("/admin/users/{id}/status", user.getPublicId())
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content("{\"active\":false}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.active").value(false));

		mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + token))
				.andExpect(status().isUnauthorized());
	}

}
