package com.example.esamepo;

import com.example.esamepo.controller.Endpoint;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Endpoint.class)
class EsamepoApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private MockMvc mvc;


	@Test
	public void ListAllAPI() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/listAll")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").exists());
	}

	@Test
	public void statsAPI() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/stats")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.max").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.average").exists());
	}

	@Test
	public void infoAPI() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/info?tld=it")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.includes").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
	}

}
