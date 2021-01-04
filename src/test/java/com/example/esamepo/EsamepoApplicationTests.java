package com.example.esamepo;

import com.example.esamepo.controller.Endpoint;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
	public void listAllAPI() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/listAll")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").exists());
	}

	@Test
	public void rankAPI() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/stats?count=4")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].domainsCount").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].description").exists());
	}

	@Test
	public void wordAPI() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.post("/stats")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"tld\":\"com\",\"words\": [\"apple\", \"chair\", \"fish\"]}"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].matchesCount").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].matchingWord").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value("3"));
	}

	@Test
	public void statsAPI() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/stats")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min.name").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min.domainsCount").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min.description").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.max.name").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.max.domainsCount").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.max.description").exists())
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
