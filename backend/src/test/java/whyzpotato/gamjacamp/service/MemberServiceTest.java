package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @PersistenceContext
    private EntityManager em;

    @Test
    void toOwner() {
        Member member = new Member("default@potato.com", "defaultMember", "p", Role.CUSTOMER);
        em.persist(member);

        memberService.toOwner(member.getId());

        assertThat(member.getRole())
                .isEqualTo(Role.OWNER);
    }
}
