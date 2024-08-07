package whyzpotato.gamjacamp.config.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

/**
 * 세션 저장소에 담아둘 인증된 멤버 정보 value
 */

@Getter
@NoArgsConstructor
public class SessionMember {
    private Long id;
    private String name;
    private String account;
    private String picture;
    private String role;

    public SessionMember(Member member) {
        this.id = member.getId();
        this.name = member.getUsername();
        this.account = member.getAccount();
        this.picture = member.getPicture();
        this.role = member.getRole().name();
    }
}
