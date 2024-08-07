package whyzpotato.gamjacamp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.ReservationStatus;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;

import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByMemberOrderByCreatedTimeDesc(Member member, Pageable pageable);

    Page<Reservation> findByCampAndStatusOrderByStayStartsDesc(Camp camp, ReservationStatus status, Pageable pageable);

    Long countByRoomAndStayEndsGreaterThanAndStayStartsLessThan(Room room, LocalDate start, LocalDate end);
}
