package whyzpotato.gamjacamp.domain.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.BaseTimeEntity;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Post;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member host;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatMember> chatMemberList = new ArrayList<ChatMember>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post = null;

    @Column(length = 20)
    private String title;

    @Max(10)
    @Min(2)
    private int capacity;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessage = null;

    @Builder
    private Chat(Member host, ChatType type, String title, int capacity, Post post) {
        this.host = host;
        this.type = type;
        this.title = title;
        this.capacity = capacity;
        this.post = post;
    }

    // user - user
    public static Chat createPublicChat(Member host, String title, int capacity, Post post) {
        if (capacity > 10)
            throw new IllegalArgumentException();
        Chat chat = Chat.builder()
                .host(host).type(ChatType.GROUP).title(title).capacity(capacity).post(post)
                .build();
        chat.chatMemberList.add(new ChatMember(chat, host, title));
        return chat;
    }

    // camp host - camp customer
    public static Chat createPrivateChat(Member sender, Member receiver) {
        Chat chat = Chat.builder()
                .host(sender).type(ChatType.SINGLE).title(receiver.getUsername()).capacity(2)
                .build();
        chat.chatMemberList.add(new ChatMember(chat, sender, receiver));
        chat.chatMemberList.add(new ChatMember(chat, receiver, sender));
        return chat;
    }

    public void setLastMessage(Message message) {
        this.lastMessage = message;
    }

    public void updateCapacity(int capacity) {
        if (capacity > 100 || capacity < chatMemberList.size())
            throw new IllegalArgumentException();
        this.capacity = capacity;
    }

    public boolean isParticipant(Member member) {
        List<Member> members = chatMemberList.stream().map(cm -> cm.getMember()).collect(Collectors.toList());
        return members.contains(member);
    }

    public Chat enter(Member member) {
        if (this.capacity <= chatMemberList.size())
            throw new IllegalStateException();
        if (isParticipant(member))
            throw new IllegalStateException();

        chatMemberList.add(new ChatMember(this, member, this.title));
        return this;
    }

    public void setPostNull() {
        this.post = null;
    }

}
