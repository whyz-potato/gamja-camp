package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.RoomDto;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomResponse;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSaveRequest;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSearchResponse;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.service.RoomService;

import javax.validation.Valid;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/rooms")
    public ResponseEntity<RoomSearchResponse> availableRooms(@RequestParam("camp") Long campId,
                                                             @RequestParam(value = "check-in", defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                             @RequestParam(value = "check-out", defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                             @RequestParam(value = "guests", defaultValue = "2") int numGuest) {

        return ResponseEntity.ok(roomService.findCampAvailableRooms(campId, checkIn, checkOut, numGuest));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomResponse> roomWithAvailCount(@PathVariable Long roomId,
                                                           @RequestParam(value = "check-in", defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                           @RequestParam(value = "check-out", defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                           @RequestParam(value = "guests", defaultValue = "2") int numGuest) {

        return ResponseEntity.ok(roomService.availRoomDetail(roomId, checkIn, checkOut, numGuest));
    }

    @PostMapping("/owner/rooms/{campId}")
    public ResponseEntity createRoom(@PathVariable Long campId,
                                     @Valid @RequestBody RoomSaveRequest request) {

        return ResponseEntity.ok(new CreatedBodyDto(roomService.saveRoom(campId, request)));
    }

    @GetMapping("/owner/rooms")
    public ResponseEntity<PageResult<RoomDto.RoomInfo>> ownerRooms(@LoginMember SessionMember owner,
                                                                   @PageableDefault Pageable pageable) {

        Page<RoomDto.RoomInfo> ownerRooms = roomService.findOwnerRooms(owner.getId(), pageable);
        return ResponseEntity.ok(new PageResult<>(ownerRooms));
    }

    @Data
    protected static class CreatedBodyDto {
        private Long id;

        public CreatedBodyDto(Long id) {
            this.id = id;
        }
    }
}
