package com.example.demo.Controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();

    @Test
    void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("token", "some-token");

        mockMvc.perform(post("/api/auth/logout").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successfully"))
                .andExpect(request -> {
                    MockHttpSession clearedSession = (MockHttpSession) request.getRequest().getSession();
                    assert clearedSession.getAttribute("token") == null;
                });
    }
}
