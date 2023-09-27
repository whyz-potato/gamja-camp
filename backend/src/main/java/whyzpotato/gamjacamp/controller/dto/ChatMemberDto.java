package whyzpotato.gamjacamp.controller.dto;

import lombok.Data;
import whyzpotato.gamjacamp.controller.dto.ChatMessageDto.DetailMessageDto;
import whyzpotato.gamjacamp.domain.chat.ChatMember;
import whyzpotato.gamjacamp.domain.member.Member;

import java.util.List;

public class ChatMemberDto {

    @Data
    public static class SimpleChatMember {

        private Long id;
        private String picture;
        private String username;

        public SimpleChatMember(ChatMember chatMember) {
            this.id = chatMember.getMember().getId();
            this.picture = chatMember.getMember().getPicture();
            this.username = chatMember.getUsername();
        }

        public SimpleChatMember(Member member) {
            this.id = member.getId();
            this.picture = member.getPicture();
            this.username = member.getUsername();
        }
    }

    @Data
    public static class EnteredChat {
        private Long roomId;
        private String title;
        private int nUnreadMessages;
        private DetailMessageDto lastMessage;
        private int nParticipants;

        public EnteredChat(ChatMember chatMember, Long count) {
            this.roomId = chatMember.getChat().getId();
            this.title = chatMember.getTitle();
            this.nUnreadMessages = count.intValue();
            this.lastMessage = new DetailMessageDto(chatMember.getChat().getLastMessage());
            this.nParticipants = chatMember.getChat().getChatMemberList().size();
        }
    }

}
