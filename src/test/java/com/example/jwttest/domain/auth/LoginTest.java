package com.example.jwttest.domain.auth;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.common.error.ApiException;
import com.example.jwttest.domain.user.entity.User;
import com.example.jwttest.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    public void LoginTest() throws Exception {
        //given
        User testUser = new User("김정연", "1234", "testnickname1");
        testUser.userToAdmin();
        userRepository.save(testUser);

        String loginRequest = """
            {
                "username": "김정연",
                "password": "1234"
            }
        """;

        //when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                        .andDo(print())
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호가 달라서 로그인 실패")
    public void LoginFailTestByPassword() throws Exception {
        //given
        User testUser = new User("김정연", "1234", "testnickname1");
        userRepository.save(testUser);

        String loginRequest = """
            {
                "username": "김정연",
                "password": "WRONGPASSWORD"
            }
        """;

        //when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("username이 존재하지 않아서 로그인 실패")
    public void LoginFailTestByUsername() throws Exception {
        //given
        User testUser = new User("김정연", "1234", "testnickname1");
        userRepository.save(testUser);

        String loginRequest = """
            {
                "username": "WRONGUSERNAME",
                "password": "1234"
            }
        """;

        //when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
