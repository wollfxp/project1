package com.dataart.project1;

import com.dataart.project1.entity.dto.RegisterUserDto;
import com.dataart.project1.service.UserDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class WebSecurityTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserDataService userDataService;

    private static Authentication auth;

    @Before
    public void makeUser() {
        if (auth == null){
            auth = new UsernamePasswordAuthenticationToken("admin", "111");
            RegisterUserDto dto = new RegisterUserDto();
            dto.setPassword("111");
            dto.setEmail("admin@starships.game");
            dto.setUsername("admin");
            assert userDataService.register(dto) != null;
        }
    }

    @Test
    public void testHomepageLoadsForLoggedInUser() throws Exception {
        this.mvc.perform(get("/").with(authentication(auth)).secure(true)).andExpect(status().is(200));
    }

    @Test
    public void testHangarOpensForLoggedInUser() throws Exception {
        this.mvc.perform(get("/hangar").with(authentication(auth)).secure(true)).andExpect(content().string(containsString("Hangar")));
    }
}
