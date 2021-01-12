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
class EsamepoExceptionTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mvc;

	@Test
	public void rankAPINegativeNumber() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/rank?count=-1")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("-1 is not a valid number"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("Provide a positive integer as a count"));
	}

	@Test
	public void rankAPINotANumber() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/rank?count=a")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("a is not a valid number"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("Provide a positive integer as a count"));
	}

	@Test
	public void statsAPINegativeNumber() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/stats?count=-1")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("-1 is not a valid number"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("Provide a positive integer as a count"));
	}

	@Test
	public void statsAPINotANumber() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/stats?count=a")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("a is not a valid number"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("Provide a positive integer as a count"));
	}

	@Test
	public void infoAPINoTld() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/info")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("The selected TLD does Not Exist"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("use /listAll for a list of all tlds"));
	}

	@Test
	public void infoAPIInvalidTld() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.get("/info?tld=invalid")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("The selected TLD does Not Exist"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("use /listAll for a list of all tlds"));
	}

	@Test
	public void wordAPIInvalidTLd() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.post("/stats")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"tld\":\"invalid\",\"words\": [\"apple\", \"chair\", \"fish\"]}"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("The selected TLD does Not Exist"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("Use /listAll for a list of all TLDs"));
	}

	@Test
	public void wordAPIWrongJSON() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.post("/stats")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"words\":\"com\",\"tlds\": [\"apple\", \"chair\", \"fish\"]}"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("Invalid JSON format"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("Use format {\"tld\":\"YOUR_TLD_HERE\",\"words\": [\"WORD_1\", \"WORD_2\", ..., \"WORD_N\"]}"));
	}

	@Test
	public void wordAPINotJSON() throws Exception
	{
		mvc.perform( MockMvcRequestBuilders
				.post("/stats")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"tlds\":\"com\",\"words\": [\"apple\", \"chair\", \"fish\"}"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UserException"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("JSON parsing error"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.todo").value("Refer to https://tools.ietf.org/html/rfc8259 for the standard format"));
	}

}
