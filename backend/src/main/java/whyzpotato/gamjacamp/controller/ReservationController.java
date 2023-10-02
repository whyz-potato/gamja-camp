package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationDetail;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationRequest;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.StatusMultipleRequest;
import whyzpotato.gamjacamp.service.ReservationService;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;


    @GetMapping("/customer/reservations/{id}")
    public ReservationDetail reservationDetail(@LoginMember SessionMember sessionMember,
                                               @PathVariable Long id) {
        return reservationService.findReservation(id, sessionMember.getId());
    }


    @PostMapping("/customer/reservations")
    public ResponseEntity<?> book(@LoginMember SessionMember sessionMember,
                                  @Valid @RequestBody ReservationRequest request) {


        Long id = reservationService.createReservation(sessionMember.getId(), request);
        URI uri = ServletUriComponentsBuilder.fromUriString("/customer/reservations")
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/owner/reservations/status")
    public ResponseEntity changeReservationStatus(@LoginMember SessionMember sessionMember,
                                                  @Valid @RequestBody StatusMultipleRequest request) {

        reservationService.updateStatus(sessionMember.getId(), request.getStatus(), request.getReservations());
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/owner/reservations")).build();
    }

    @DeleteMapping("/customer/reservations/{id}")
    public ResponseEntity cancelReservation(@LoginMember SessionMember sessionMember,
                                            @PathVariable Long id){

        reservationService.cancel(sessionMember.getId(), id);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/customer/reservations/my")
    public ResponseEntity<?> reservationList(@LoginMember SessionMember sessionMember,
                                             @RequestParam(required = false, defaultValue = "5") int limit,
                                             @RequestParam(required = false, defaultValue = "0") int offset){

        return ResponseEntity.ok(reservationService.findReservations(sessionMember.getId(), limit, offset));

    }


}