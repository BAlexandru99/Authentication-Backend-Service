package com.app.HRApp;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HrAppAplicationTests{

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void contextLoads(){
        assertNotNull(mockMvc);
    }

    public void testRegister() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.post("/register")
            .param("id", "1")
            .param("username", "alex@yahoo.com")
            .param("firstName", "Alex")
            .param("lastName", "Alex")
            .param("phoneNumber", "123123123")
            .param("timestamp", null)
            .param("token", null)
            .param("isEnabled", "false");

            mockMvc.perform(request)
                .andExpect(status().isCreated());
    }
}
