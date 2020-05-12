package edu.fbansept.springsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fbansept.springsecurity.model.Utilisateur;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringsecurityApplicationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mvc;

    @BeforeEach
    public void beforeAll() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

	@Test
    @Order(1)
    //@WithMockUser(username = "admin", roles={"ADMIN"})
	void contextLoads() throws Exception {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test2");
        utilisateur.setPassword("test2");
        String json = mapper.writeValueAsString(utilisateur);

        mvc.perform(
                put("/inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        String token = mvc.perform(
                post("/authentification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(
                get("/hello")
                .header("authorization", "Bearer " + token)
        )
        .andExpect(status().isOk());
	}

}
