package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.service.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login-member")
    public ResponseEntity<?> loginMember(@LoginMember SessionMember member) {
        return ResponseEntity.ok(member);
    }

    @PatchMapping("/members/role")
    public ResponseEntity<Void> changeToOwner(@LoginMember SessionMember member) {
        memberService.toOwner(member.getId());
        return ResponseEntity.ok().build();
    }
}
