package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.CampDto.*;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.CampService;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CampController {

    private final CampService campService;
    private final AwsS3Service awsS3Service;

    @GetMapping("/camps/search")
    public CampSearchResult<CampSearchItem> search(@RequestParam(defaultValue = "") String query,
                                                   @RequestParam("ne-lat") Double neLatitude,
                                                   @RequestParam("sw-lat") Double swLatitude,
                                                   @RequestParam("ne-lng") Double neLongitude,
                                                   @RequestParam("sw-lng") Double swLongitude,
                                                   @RequestParam(value = "check-in", defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                   @RequestParam(value = "check-out", defaultValue = "#{T(java.time.LocalDate).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                   @RequestParam(value = "guests", defaultValue = "2") int numGuests,
                                                   Pageable pageable) {

        return campService.search(query, neLatitude, swLatitude, neLongitude, swLongitude, checkIn, checkOut, numGuests, pageable);
    }

    @PostMapping("/camps/new/{memberId}")
    public ResponseEntity createCamp(@PathVariable("memberId") Long memberId,
                                     @RequestPart("request") CampSaveRequest request,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        return new ResponseEntity(new createdBodyDto(campService.register(memberId, request, awsS3Service.uploadImages(multipartFiles))), HttpStatus.CREATED);
    }

    @GetMapping("/camps/{campId}")
    public ResponseEntity<CampDetail> detailCamp(@PathVariable("campId") Long campId) {
        return new ResponseEntity<>(campService.findCamp(campId), HttpStatus.OK);
    }

    @PutMapping("/camps/update/{memberId}/{campId}")
    public ResponseEntity<CampDetail> updateCamp(@PathVariable("memberId") Long memberId,
                                                 @PathVariable("campId") Long campId,
                                                 @RequestBody CampUpdateRequest request) {
        return new ResponseEntity<>(campService.updateCamp(memberId, campId, request), HttpStatus.OK);
    }

    @PutMapping("/camps/update/address/{memberId}/{campId}")
    public ResponseEntity<CampDetail> updateCampAddress(@PathVariable("memberId") Long memberId,
                                                        @PathVariable("campId") Long campId,
                                                        @RequestParam("address") String address) {
        return new ResponseEntity<>(campService.updateCampAddress(memberId, campId, address), HttpStatus.OK);
    }

    @PutMapping("/camps/update/image/{memberId}/{campId}")
    public ResponseEntity<CampDetail> updateCampImages(@PathVariable("memberId") Long memberId,
                                                       @PathVariable("campId") Long campId,
                                                       @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(campService.findCampImages(memberId, campId));
        return new ResponseEntity<>(campService.updateCampImages(memberId, campId, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/camps/delete/{memberId}/{campId}")
    public ResponseEntity deleteCamp(@PathVariable("memberId") Long memberId,
                                     @PathVariable("campId") Long campId) {
        awsS3Service.removeImages(campService.findCampImages(memberId, campId));
        campService.delete(memberId, campId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/owner/camps")
    public ResponseEntity<CampDetail> ownerCamp(@LoginMember SessionMember owner) {
        return new ResponseEntity<>(campService.findOwnerCamp(owner.getId()), HttpStatus.OK);
    }

    @Data
    protected static class createdBodyDto {
        private Long id;

        public createdBodyDto(Long id) {
            this.id = id;
        }
    }
}
