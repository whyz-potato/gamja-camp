package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.controller.dto.WriterDto.WriterSimple;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Review;
import whyzpotato.gamjacamp.domain.member.Member;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ReviewDto {

    @Getter
    @NoArgsConstructor
    public static class ReviewSimple {
        private Long id;
        private WriterSimple writer;
        private LocalDate date;
        private LocalTime time;
        private Long camp;
        private Long reservation;
        private int rate;

        @Builder
        public ReviewSimple(Long id, Member writer, Long camp, Long reservation, int rate, LocalDate date, LocalTime time) {
            this.id = id;
            this.writer = new WriterSimple(writer);
            this.date = date;
            this.time = time;
            this.camp = camp;
            this.reservation = reservation;
            this.rate = rate;
        }

        public Page<ReviewSimple> toList(Page<Review> reviews) {
            Page<ReviewSimple> reviewSimpleList = reviews.map(
                    m-> ReviewSimple.builder()
                            .id(m.getId())
                            .writer(m.getWriter())
                            .date(m.getCreatedTime().toLocalDate())
                            .time(m.getCreatedTime().toLocalTime())
                            .camp(m.getCamp().getId())
                            .reservation(m.getReservation().getId())
                            .rate(m.getRate())
                            .build());
            return reviewSimpleList;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewDetail {
        private Long id;
        private WriterSimple writer;
        private LocalDate date;
        private LocalTime time;
        private Long camp;
        private Long reservation;
        private int rate;
        private String content;
        private List<String> images;

        @Builder
        public ReviewDetail(Long id, Member writer, Camp camp, Reservation reservation, int rate, String content, List<Image> images, LocalDate date, LocalTime time) {
            this.id = id;
            this.writer = new WriterSimple(writer);
            this.date = date;
            this.time = time;
            this.camp = camp.getId();
            this.reservation = reservation.getId();
            this.rate = rate;
            this.content = content;
            if(!images.isEmpty()) {
                this.images = images.stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
        }

        public ReviewDetail(Review review) {
            this.id = review.getId();
            this.writer = new WriterSimple(review.getWriter());
            this.date = review.getCreatedTime().toLocalDate();
            this.time = review.getCreatedTime().toLocalTime();
            this.camp = review.getCamp().getId();
            this.reservation = review.getReservation().getId();
            this.rate = review.getRate();
            this.content = review.getContent();
            if(!review.getImages().isEmpty()) {
                this.images = review.getImages().stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewSaveRequest {
        private int rate;
        private String content;

        @Builder
        public ReviewSaveRequest(int rate, String content) {
            this.rate = rate;
            this.content = content;
        }

        public Review toEntity(Member member, Camp camp, Reservation reservation) {
            return Review.builder()
                    .writer(member)
                    .camp(camp)
                    .reservation(reservation)
                    .rate(rate)
                    .content(content)
                    .images(new ArrayList<>())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewUpdateRequest {
        private int rate;
        private String content;

        @Builder
        public ReviewUpdateRequest(int rate, String content) {
            this.rate = rate;
            this.content = content;
        }
    }
}                                    
              
