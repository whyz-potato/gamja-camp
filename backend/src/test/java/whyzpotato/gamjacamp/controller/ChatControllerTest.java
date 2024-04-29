package whyzpotato.gamjacamp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ChatDto;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.chat.Message;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.domain.post.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private Member sender, receiver;
    private Chat privateChat;
    private Chat publicChat;
    private Post post;

    @BeforeEach
    void setUp() {
        sender = new Member("customer@potato.com", "sender", "", Role.CUSTOMER);
        receiver = new Member("a", "receiver", "", Role.CUSTOMER);
        post = Post.builder().writer(sender).title("title").content("content").images(new ArrayList<>()).build();
        privateChat = Chat.createPrivateChat(sender, receiver);
        publicChat = Chat.createPublicChat(sender, "모집 채팅", 10, post);
        em.persist(sender);
        em.persist(receiver);
        em.persist(post);
        em.persist(privateChat);
        em.persist(publicChat);

        session = new MockHttpSession();
    }

    @AfterEach
    void tearDown() {
        em.clear();
        session.clearAttributes();
    }

    @Test
    void createPrivateChat() throws Exception {
        String request = objectMapper.writeValueAsString(new ChatDto.PrivateChatRequest(receiver.getId()));
        session.setAttribute("member", new SessionMember(sender));

        mockMvc.perform(post("/chats/single")
                        .session(session)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void createPublicChat() throws Exception {
        String request = objectMapper.writeValueAsString(new ChatDto.PublicChatRequest(post.getId(), 2));
        session.setAttribute("member", new SessionMember(sender));

        mockMvc.perform(post("/chats/group")
                        .with(csrf())
                        .session(session)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getMassages() throws Exception {
        String uri = String.format("/chats/%d", privateChat.getId());
        session.setAttribute("member", new SessionMember(sender));

        for (int i = 0; i < 20; i++) {
            em.persist(new Message(privateChat, sender, (i + 1) + "번 메세지"));
        }

        mockMvc.perform(get(uri).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfElements").value(10))
                .andExpect(jsonPath("hasNext").value("true"))
                .andDo(print());
    }

    @Test
    void getMassagesBefore() throws Exception {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Message message = new Message(privateChat, sender, (i + 1) + "번 메세지");
            em.persist(message);
            messages.add(message);
        }

        String fifthId = messages.get(5).getId().toString();

        String uri = String.format("/chats/%d", privateChat.getId());
        session.setAttribute("member", new SessionMember(sender));
        mockMvc.perform(get(uri).session(session)
                        .param("before", fifthId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfElements").value(5))
                .andExpect(jsonPath("hasNext").value("false"))
                .andDo(print());
    }

    @Test
    @Rollback(false)
    @DisplayName("안읽은메세지 쌓인 경우")
    void getMassages_unread() throws Exception {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Message message = new Message(privateChat, sender, (i + 1) + "번 메세지");
            em.persist(message);
            messages.add(message);
        }
        ChatMember chatMember = privateChat.getChatMemberList().get(1);
        Message fifth = messages.get(5);
        chatMember.updateLastReadMessage(fifth);

        String uri = String.format("/chats/%d", privateChat.getId());
        session.setAttribute("member", new SessionMember(receiver));
        mockMvc.perform(get(uri).session(session)
                        .param("max-unread", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isStacked").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(10))
                .andExpect(jsonPath("$.hasNext").value("true"))
                .andExpect(jsonPath("$.messages[0].id").value(fifth.getId()))
                .andDo(print());
    }


    @Test
    void 채팅방나가기() throws Exception {
        String uri = String.format("/chats/%d/members", privateChat.getId());

        session.setAttribute("member", new SessionMember(receiver));
        mockMvc.perform(delete(uri)
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    void 채팅방나가기_방장() throws Exception {
        String uri = String.format("/chats/%d/members", privateChat.getId());
        session.setAttribute("member", new SessionMember(sender));
        mockMvc.perform(delete(uri)
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    void 채팅방삭제_방장() throws Exception {
        String uri = String.format("/chats/%d", privateChat.getId());
        session.setAttribute("member", new SessionMember(sender));
        mockMvc.perform(delete(uri)
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }


    @Test
    void 채팅방삭제_방장아님_실패() throws Exception {
        String uri = String.format("/chats/%d", privateChat.getId());
        session.setAttribute("member", new SessionMember(receiver));
        mockMvc.perform(delete(uri).session(session))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void 안읽은메세지() throws Exception {
        String uri = "/chats/num-unread";
        session.setAttribute("member", new SessionMember(receiver));
        mockMvc.perform(get(uri).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("numUnread").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 목록")
    void chats() throws Exception {
        String uri = "/chats";
        session.setAttribute("member", new SessionMember(sender));
        mockMvc.perform(get(uri).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chats").exists())
                .andExpect(jsonPath("$.chats[0]").exists())
                .andExpect(jsonPath("$.chats[1]").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 목록 순서1_마지막메세지 역순, 채팅방 생성 역순")
    void chatsSorted() throws Exception {
        String uri = "/chats";
        session.setAttribute("member", new SessionMember(sender));
        mockMvc.perform(get(uri).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chats").exists())
                .andExpect(jsonPath("$.chats[0].title").value(publicChat.getTitle()))
                .andExpect(jsonPath("$.chats[1].title").value("receiver"))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 목록 순서2_마지막메세지 역순, 채팅방 생성 역순")
    void chatsSorted2() throws Exception {
        em.persist(new Message(privateChat, sender, "hello"));

        String uri = "/chats";
        session.setAttribute("member", new SessionMember(sender));
        mockMvc.perform(get(uri).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chats").exists())
                .andExpect(jsonPath("$.chats[0].title").value("receiver"))
                .andExpect(jsonPath("$.chats[1].title").value(publicChat.getTitle()))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글로 채팅 정보 조회")
    void test() throws Exception {
        String uri = String.format("/chats/group/%d", post.getId());
        session.setAttribute("member", new SessionMember(sender));

        mockMvc.perform(get(uri)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roomId").value(publicChat.getId()))
                .andExpect(jsonPath("capacity").value(10))
                .andDo(print());
    }
}
