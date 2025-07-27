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
public class SignupTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 성공")
    public void signupTest() throws Exception {
        //given
        String signupRequest = """
            {
                "username": "김정연",
                "password": "1234",
                "nickname": "test1"
            }
        """;

        //when & then
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequest))
                .andDo(print())
                .andExpect(status().isOk());

        User user = userRepository.findByUsername("김정연")
                .orElseThrow(() -> new ApiException(ErrorStatus.INVALID_CREDENTIALS));

        assertEquals("김정연", user.getUsername());
    }

    @Test
    @DisplayName("username이 중복되면 회원가입에 실패한다.")
    public void signupFailTest() throws Exception {
        //given
        User testuser = new User("김정연", "12345", "testnickname");
        userRepository.save(testuser);

        String signupRequest = """
            {
                "username": "김정연",
                "password": "1234",
                "nickname": "test1"
            }
        """;

        //when & then
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
