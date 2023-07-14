package whyzpotato.gamjacamp.domain.chat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member host;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatMember> chatMemberList = new ArrayList<ChatMember>();

    @Column(length = 20)
    private String title;

    @Max(10) @Min(2)
    private int capacity;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessage = null;

    private Chat(Member host, String title, int capacity) {
        this.host = host;
        this.title = title;
        this.capacity = capacity;
    }

    // user - user
    public static Chat createPublicChat(Member host, String title, int capacity) {
        if (capacity > 10)
            throw new IllegalArgumentException();
        Chat chat = new Chat(host, title, capacity);
        chat.chatMemberList.add(new ChatMember(chat, host, title));
        return chat;
    }

    // camp host - camp customer
    public static Chat createPrivateChat(Member host, Member customer) {
        Chat chat = new Chat(host, "", 2);
        chat.chatMemberList.add(new ChatMember(chat, host, customer.getUsername()));
        chat.chatMemberList.add(new ChatMember(chat, customer, host.getUsername()));
        return chat;
    }

    public void setLastMessage(Message message) {
        this.lastMessage = message;
    }

    public void updateCapacity(int capacity) {
        if (capacity > 100 || capacity < this.chatMemberList.size())
            throw new IllegalArgumentException();
        this.capacity = capacity;
    }

    public boolean enter(Member member) {
        if (this.capacity >= chatMemberList.size())
            return false;
        chatMemberList.add(new ChatMember(this, member, this.title));
        return true;
    }
}
