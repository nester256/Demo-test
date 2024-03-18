package com.example.demo_test;

import com.example.demo.DemoApplication;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = DemoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class DemoTestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Order(1)
	public void testCreateService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/services")
						.content("{ \"name\": \"TestService\", \"language\": \"Java\" }")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	@Order(2)
	public void testGetAllServices() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/services")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@Order(3)
	public void testGetServiceById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/services/{id}", 6)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@Order(4)
	public void testUpdateService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/services/{id}", 6)
						.content("{\"id\": 1, \"name\": \"UpdatedService\", \"language\": \"Python\" }")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@Order(5)
	public void testDeleteService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/services/{id}", 6))
				.andExpect(status().isOk());
	}

}



