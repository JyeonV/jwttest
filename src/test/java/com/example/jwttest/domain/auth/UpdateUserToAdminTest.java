package com.example.jwttest.domain.auth;

import com.example.jwttest.common.code.ErrorStatus;
import com.example.jwttest.common.error.ApiException;
import com.example.jwttest.common.jwt.JwtUtil;
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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateUserToAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("권한 변경 성공")
    public void adminApiTest() throws Exception {
        //given
        User testUser = new User("testUser", "1234", "testnickname1");
        User adminUser = new User("testAdmin", "1234", "testnickname2");
        adminUser.userToAdmin();
        userRepository.save(testUser);
        userRepository.save(adminUser);

        String accessToken = jwtUtil.createAccessToken(adminUser.getId(), adminUser.getUsername(), adminUser.getUserRole());

        //when & then
        mockMvc.perform(patch("/api/admin/users/"+ testUser.getId() + "/roles")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User 권한으로 Admin 권한 api 호출 시 실패")
    public void adminApiTestFailByAuthorization() throws Exception {
        //given
        User testUser = new User("testUser", "1234", "testnickname1");
        User adminUser = new User("testAdmin", "1234", "testnickname2");
        userRepository.save(testUser);
        userRepository.save(adminUser);

        String accessToken = jwtUtil.createAccessToken(adminUser.getId(), adminUser.getUsername(), adminUser.getUserRole());

        //when & then
        mockMvc.perform(patch("/api/admin/users/"+ testUser.getId() + "/roles")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("토큰 없이 접근할 시 실패")
    public void adminApiTestFailByWithoutAccessToken() throws Exception {
        //given
        User testUser = new User("testUser", "1234", "testnickname1");
        User adminUser = new User("testAdmin", "1234", "testnickname2");
        adminUser.userToAdmin();
        userRepository.save(testUser);
        userRepository.save(adminUser);

        String accessToken = "";

        //when & then
        mockMvc.perform(patch("/api/admin/users/1/roles") // userId는 테스트에 맞게 조정
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("만료된 어세스 토큰으로 호출 시 실패")
    public void adminApiTestFailByExpiredAccessToken() throws Exception {
        //given
        User testUser = new User("testUser", "1234", "testnickname1");
        User adminUser = new User("testAdmin", "1234", "testnickname2");
        adminUser.userToAdmin();
        userRepository.save(testUser);
        userRepository.save(adminUser);

        String accessToken = jwtUtil.createExpiredAccessToken(adminUser.getId(), adminUser.getUsername(), adminUser.getUserRole());

        //when & then
        mockMvc.perform(patch("/api/admin/users/1/roles") // userId는 테스트에 맞게 조정
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("잘못된 형식의 어세스 토큰으로 호출 시 실패")
    public void adminApiTestFailByWrongAccessToken() throws Exception {
        //given
        User testUser = new User("testUser", "1234", "testnickname1");
        User adminUser = new User("testAdmin", "1234", "testnickname2");
        adminUser.userToAdmin();
        userRepository.save(testUser);
        userRepository.save(adminUser);

        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.hnJkhwoJ8rYV34aZsF";

        //when & then
        mockMvc.perform(patch("/api/admin/users/1/roles") // userId는 테스트에 맞게 조정
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
