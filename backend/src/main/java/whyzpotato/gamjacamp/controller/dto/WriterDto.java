package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

public class WriterDto {

    @Getter
    @NoArgsConstructor
    public static class WriterSimple {
        private Long id;
        private String username;
        private String profile;

        @Builder
        public WriterSimple(Long id, String username, String profile) {
            this.id = id;
            this.username = username;
            this.profile = profile;
        }

        public WriterSimple(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.profile = member.getPicture();
        }
    }


}
