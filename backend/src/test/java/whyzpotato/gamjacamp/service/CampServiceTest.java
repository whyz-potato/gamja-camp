package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.controller.dto.camp.CampDto;
import whyzpotato.gamjacamp.controller.dto.camp.CampSaveRequestDto;
import whyzpotato.gamjacamp.controller.dto.camp.CampUpdateRequestDto;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CampServiceTest {

    @Autowired
    private CampService campService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CampRepository campRepository;

    @Test
    public void 캠핑장등_등록() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        CampSaveRequestDto campSaveRequestDto = CampSaveRequestDto.builder().name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").build();

        Long campId = campService.register(member.getId(), campSaveRequestDto);

        Optional<Camp> camp = campRepository.findById(campId);
        assertThat(camp.get().getMember().getId()).isEqualTo(member.getId());
        assertThat(camp.get().getName()).isEqualTo(campSaveRequestDto.getName());
        assertThat(camp.get().getAddress()).isEqualTo(campSaveRequestDto.getAddress());
    }

    @Test void 캠핑장_정보조회() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152).latitude(92.1234455).build());

        CampDto campDto = campService.findCamp(member.getId());

        assertThat(campDto.getId()).isEqualTo(camp.getId());
        assertThat(campDto.getMemberId()).isEqualTo(member.getId());
        assertThat(campDto.getName()).isEqualTo("감자캠핑");
        assertThat(campDto.getAddress()).isEqualTo("서울특별시 광진구 동일로40길 25-1");
        assertThat(campDto.getLongitude()).isEqualTo(126.1332152);
        assertThat(campDto.getLatitude()).isEqualTo(92.1234455);
    }

    @Test
    public void 캠핑장_정보변경() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152f).latitude(92.1234455).build());

        campService.updateCamp(member.getId(), camp.getId(), CampUpdateRequestDto.builder().name("이름변경").phone("010-1234-1234").campIntroduction("캠프소개").build());

        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
        assertThat(updateCamp.get().getName()).isEqualTo("이름변경");
        assertThat(updateCamp.get().getPhone()).isEqualTo("010-1234-1234");
        assertThat(updateCamp.get().getCampIntroduction()).isEqualTo("캠프소개");
    }

    @Test
    public void 캠핑장_정보변경_연락처소개_삭제() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").phone("010-1234-1234").campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455).build());

        campService.updateCamp(member.getId(), camp.getId(), CampUpdateRequestDto.builder().name("감자캠핑").build());

        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
        assertThat(updateCamp.get().getName()).isEqualTo("감자캠핑");
        assertThat(updateCamp.get().getPhone()).isNull();
        assertThat(updateCamp.get().getCampIntroduction()).isNull();
    }

    @Test
    public void 캠핑장_주소변경() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 군자동 218").longitude(126.1332152).latitude(92.1234455).build());

        campService.updateCampAddress(member.getId(), camp.getId(), "서울특별시 광진구 동일로40길 25-1");

        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
        assertThat(updateCamp.get().getAddress()).isEqualTo("서울특별시 광진구 동일로40길 25-1");
        System.out.println(updateCamp.get().getLongitude());
        System.out.println(updateCamp.get().getLatitude());
    }

    @Test
    public void 캠핑장_운영시간변경() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152).latitude(92.1234455).build());

        campService.updateOperatingHours(member.getId(),  camp.getId(), 11, 0, 22, 30);

        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
        assertThat(updateCamp.get().getCampOperationStart()).isEqualTo(LocalTime.of(11,0));
        assertThat(updateCamp.get().getCampOperationEnd()).isEqualTo(LocalTime.of(22,30));
    }

    @Test
    public void 캠핑장_삭제() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152).latitude(92.1234455).build());

        campService.delete(member.getId(), camp.getId());

        Optional<Camp> deleteCamp = campRepository.findById(camp.getId());
        assertThat(deleteCamp.isPresent()).isFalse();
    }

    @Test
    public void 캠핑장_운영시간삭제() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152).latitude(92.1234455).build());
        campService.updateOperatingHours(member.getId(),  camp.getId(), 11, 0, 22, 30);

        campService.deleteOperatingHours(member.getId(), camp.getId());

        Optional<Camp> optionalCamp = campRepository.findById(camp.getId());
        assertThat(optionalCamp.get().getCampOperationStart()).isNull();
        assertThat(optionalCamp.get().getCampOperationEnd()).isNull();
    }
}