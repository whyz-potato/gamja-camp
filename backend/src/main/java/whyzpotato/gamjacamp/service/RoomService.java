package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.PriceDto;
import whyzpotato.gamjacamp.controller.dto.RoomDto;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomDetail;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomResponse;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSaveRequest;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSearchResponse;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final CampRepository campRepository;
    private final MemberRepository memberRepository;

    public Long saveRoom(Long campId, RoomSaveRequest request) {
        Room room = request.toEntity();
        if (request.getId() != null) {
            Room updateRoom = roomRepository.findById(request.getId()).orElseThrow(NotFoundException::new);
            updateRoom.update(room);
            return updateRoom.getId();
        }

        room.setCamp(campRepository.findById(campId).orElseThrow(NotFoundException::new));
        roomRepository.save(room);
        return room.getId();
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("잘못된 객실 정보입니다."));
    }

    public RoomSearchResponse findCampAvailableRooms(Long campId, LocalDate checkIn, LocalDate checkOut, int numGuests) {
        List<Room> availRooms = roomRepository.findAvailRooms(campId, checkIn, checkOut, numGuests);
        return toRoomSearchResponse(campId, availRooms, checkIn, checkOut);
    }

    public RoomResponse availRoomDetail(Long roomId, LocalDate checkIn, LocalDate checkOut, int numGuests) {
        Room room = roomRepository.findById(roomId).orElseThrow(NotFoundException::new);
        Long count = roomRepository.countAvailRoom(roomId, checkIn, checkOut, numGuests);
        return toRoomResponse(room, checkIn, checkOut, count);
    }

    @Transactional(readOnly = true)
    public Page<RoomDto.RoomInfo> findOwnerRooms(Long ownerId, Pageable pageable) {
        Member owner = memberRepository.findById(ownerId).orElseThrow(NotFoundException::new);
        Camp camp = campRepository.findByMember(owner).orElseThrow(NotFoundException::new);
        return roomRepository.findAllByCamp(camp, pageable).map(c -> new RoomDto.RoomInfo(c));
    }

    private RoomSearchResponse toRoomSearchResponse(Long campId, List<Room> rooms, LocalDate checkIn, LocalDate checkOut) {
        List<RoomDetail> roomDetails = rooms.stream()
                .map(r -> toRoomDetail(r, checkIn, checkOut))
                .collect(Collectors.toList());
        return new RoomSearchResponse(checkIn, checkOut, campId, roomDetails);
    }

    private RoomResponse toRoomResponse(Room room, LocalDate checkIn, LocalDate checkOut, Long cnt) {
        return new RoomResponse(checkIn, checkOut, cnt.intValue(), toRoomDetail(room, checkIn, checkOut));
    }

    private RoomDetail toRoomDetail(Room room, LocalDate stayStarts, LocalDate stayEnds) {
        PriceDto priceDto = new PriceDto(room.getPrices(stayStarts, stayEnds));
        return new RoomDetail(room.getId(), room.getName(), room.getCapacity(), priceDto);
    }
}
