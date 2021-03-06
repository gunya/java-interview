package com.hepexta.interview.spring.web.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.servlet.ServletContext;

import com.hepexta.interview.spring.web.config.WebConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfig.class, WebConfig.class })
public class GreetControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    /*
    * perform() method will call a get request method which returns the ResultActions.
    * Using this result we can have assertion expectations on response like content, HTTP status, header, etc
    *
    * andDo(print()) will print the request and response. This is helpful to get detailed view in case of error
    * andExpect() will expect the provided argument. In our case we are expecting “index” to be returned via MockMvcResultMatchers.view()
    * */
    private MockMvc mockMvc;

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void givenWAC_whenServletContext_thenItProvidesGreetController() {
        final ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("greetController"));
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsIndexJSPViewName() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/homePage"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    public void givenGreetURI_whenMockMVC_thenVerifyResponse() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/greet").contentType(CONTENT_TYPE))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello World!!!"))
                .andReturn();
        Assert.assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenGreetURIWithPathVariable_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/greetWithPathVariable/John"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(CONTENT_TYPE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello World John!!!"));
    }

    @Test
    public void givenGreetURIWithPathVariable_2_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/greetWithPathVariable/{name}", "Doe"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(CONTENT_TYPE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello World Doe!!!"));
    }

    @Test
    public void givenGreetURIWithQueryParameter_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/greetWithQueryVariable").contentType(CONTENT_TYPE)
                .param("name", "John Doe"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(CONTENT_TYPE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello World John Doe!!!"));
    }

    @Test
    public void givenGreetURIWithPost_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/greetWithPost"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(CONTENT_TYPE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello World!!!"));
    }

    @Test
    public void givenGreetURIWithPostAndFormData_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/greetWithPostAndFormData")
                .param("id", "1").param("name", "John Doe"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(CONTENT_TYPE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello World John Doe!!!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void givenGreetURI_whenMockMVCWithUnsupportedMediaType_thenException() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/greet").contentType(MediaType.APPLICATION_PDF))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    }
}
