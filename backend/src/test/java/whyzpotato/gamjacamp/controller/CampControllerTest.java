package whyzpotato.gamjacamp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CampControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private Member host;
    private Member host2;
    private Member customer;
    private Camp camp1;
    private Camp camp2;
    private Camp camp3;
    private Camp camp4;
    private Camp camp5;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();

        host = Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build();
        host2 = Member.builder().account("host2@test.com").username("host2").picture(null).role(Role.OWNER).build();
        customer = Member.builder().account("customer@mail.com").role(Role.CUSTOMER).username("customer").picture("img")
                .build();
        camp1 = Camp.builder().member(host).name("감자캠핑1").address("서울특별시 동일로40길 25-1").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(37.1234455).build();
        camp2 = Camp.builder().member(host2).name("감자캠핑2").address("서울특별시 광진구").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(37.1234455).build();
        camp3 = Camp.builder().member(host2).name("감자캠핑3").address("부산광역시 중구").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(37.1234455).build();
        camp4 = Camp.builder().member(host2).name("감자캠핑4").address("경기도 구리시").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(37.1234455).build();
        camp5 = Camp.builder().member(host2).name("감자캠핑5").address("강원도 춘천시").phone("010-1234-1234")
                .campIntroduction("캠프소개").longitude(126.1332152f).latitude(37.1234455).build();
        em.persist(host);
        em.persist(host2);
        em.persist(customer);
        em.persist(camp1);
        em.persist(camp2);
        em.persist(camp3);
        em.persist(camp4);
        em.persist(camp5);

        em.persist(Room.builder()
                .name("camp1_room1").camp(camp1).capacity(2).cnt(2).weekPrice(5000).weekendPrice(10000).build());
        em.persist(Room.builder()
                .name("camp2_room1").camp(camp2).capacity(3).cnt(2).weekPrice(5000).weekendPrice(10000).build());
        em.persist(Room.builder()
                .name("camp3_room1").camp(camp3).capacity(4).cnt(2).weekPrice(5000).weekendPrice(10000).build());
        em.persist(Room.builder()
                .name("camp4_room1").camp(camp4).capacity(5).cnt(2).weekPrice(5000).weekendPrice(10000).build());
        em.persist(Room.builder()
                .name("camp5_room1").camp(camp5).capacity(6).cnt(2).weekPrice(5000).weekendPrice(10000).build());
    }

    @AfterEach
    void tearDown() {
        em.clear();
        session.clearAttributes();
    }

    @DisplayName("캠핑장 검색")
    @Test
    void searchCamp() throws Exception {
        String uri = "/camps/search";
        mockMvc.perform(get(uri)
                        .param("query", "")
                        .param("ne-lat", "43.00")
                        .param("sw-lng", "124.0")
                        .param("ne-lng", "132.0")
                        .param("sw-lat", "33.00")
                        .param("check-in", LocalDate.now().toString())
                        .param("check-out", LocalDate.now().plusDays(1).toString())
                        .param("guests", "3")
                        .param("page", "1")
                        .param("size", "2")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.isLast").value(true))
                .andExpect(jsonPath("$.content[*].rate").exists())
                .andDo(print());
    }

    @DisplayName("캠핑장 검색_기본 파라미터")
    @Test
    void searchCamp_defaultParameter() throws Exception {
        String uri = "/camps/search";
        mockMvc.perform(get(uri)
                        .param("ne-lat", "43.00")
                        .param("sw-lng", "124.0")
                        .param("ne-lng", "132.0")
                        .param("sw-lat", "33.00")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.numberOfElements").value(5))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andDo(print());
    }

    @DisplayName("사장 캠핑장 조회")
    @WithMockUser(roles = {"OWNER"})
    @Test
    void ownerCamp() throws Exception {
        String uri = "/owner/camps";
        session.setAttribute("member", new SessionMember(host));
        mockMvc.perform(get(uri)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("감자캠핑1"))
                .andDo(print());
    }

    @DisplayName("사장 캠핑장 조회_캠핑장이 없는 경우")
    @WithMockUser(roles = {"OWNER"})
    @Test
    void ownerNoCamp() throws Exception {
        String uri = "/owner/camps";
        session.setAttribute("member", new SessionMember(customer));
        mockMvc.perform(get(uri)
                        .session(session)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
