package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.controller.dto.WriterDto.WriterSimple;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.chat.Chat;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GatherPostDto {

    //GatherPostSimple
    @Getter
    @NoArgsConstructor
    public static class GatherPostSimple {
        private Long id;
        private WriterSimple writer;
        private LocalDate date;
        private LocalTime time;
        private String title;
        private String content;
        private String image;

        @Builder
        public GatherPostSimple(Long id, Member writer, String title, String content, List<Image> images, LocalDate date, LocalTime time) {
            this.id = id;
            this.writer = new WriterSimple(writer);
            this.date = date;
            this.time = time;
            this.title = title;
            this.content = content;
            if (!images.isEmpty()) {
                this.image = images.get(0).getPath();   //첫번째 사진
            }
        }

        public GatherPostSimple(Post post) {
            this.id = post.getId();
            this.writer = new WriterSimple(post.getWriter());
            this.date = post.getCreatedTime().toLocalDate();
            this.time = post.getCreatedTime().toLocalTime();
            this.title = post.getTitle();
            this.content = post.getContent();
            if (!post.getImages().isEmpty()) {
                this.image = post.getImages().get(0).getPath();   //첫번째 사진
            }
        }

        public Page<GatherPostSimple> toList(Page<Post> posts) {
            Page<GatherPostSimple> gatherPostSimpleList = posts.map(
                    m -> GatherPostSimple.builder()
                            .id(m.getId())
                            .writer(m.getWriter())
                            .title(m.getTitle())
                            .content(m.getContent())
                            .images(m.getImages())
                            .date(m.getCreatedTime().toLocalDate())
                            .time(m.getCreatedTime().toLocalTime())
                            .build());
            return gatherPostSimpleList;
        }
    }

    //GatherPostDetail
    @Getter
    @NoArgsConstructor
    public static class GatherPostDetail {
        private Long id;
        private WriterSimple writer;
        private LocalDate date;
        private LocalTime time;
        private String title;
        private String content;
        private PostType postType;
        private List<String> images;
//        private Long chat;

//        @Builder
//        public GatherPostDetail(Long id, Member writer, String title, String content, PostType postType, List<Image> images, Chat chat, LocalDate date, LocalTime time) {
//            this.id = id;
//            this.writer = new WriterSimple(writer);
//            this.date = date;
//            this.time = time;
//            this.title = title;
//            this.content = content;
//            this.postType = postType;
//            if(!images.isEmpty()) {
//                this.images = images.stream()
//                        .map(Image::getPath)
//                        .collect(Collectors.toList());
//            }
//            this.chat = chat.getId();
//        }

        @Builder
        public GatherPostDetail(Long id, Member writer, String title, String content, PostType postType, List<Image> images, LocalDate date, LocalTime time) {
            this.id = id;
            this.writer = new WriterSimple(writer);
            this.date = date;
            this.time = time;
            this.title = title;
            this.content = content;
            this.postType = postType;
            if(!images.isEmpty()) {
                this.images = images.stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
        }

        public GatherPostDetail(Post post) {
            this.id = post.getId();
            this.writer = new WriterSimple(post.getWriter());
            this.date = post.getCreatedTime().toLocalDate();
            this.time = post.getCreatedTime().toLocalTime();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.postType = post.getType();
            if(!post.getImages().isEmpty()) {
                this.images = post.getImages().stream()
                        .map(Image::getPath)
                        .collect(Collectors.toList());
            }
//            if(post.getChat() != null ) {
//                this.chat = post.getChat().getId();
//            }
        }
    }

    //GatherPostSaveRequest
    @Getter
    @NoArgsConstructor
    public static class GatherPostSaveRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;

        @Builder
        public GatherPostSaveRequest(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public Post toEntity(Member member, PostType postType) {
            return Post.builder()
                    .writer(member)
                    .title(this.title)
                    .content(this.content)
                    .type(postType)
                    .images(new ArrayList<Image>())
                    .comments(new ArrayList<Comment>())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class GatherPostUpdateRequest {
        private String title;
        private String content;

        @Builder

        public GatherPostUpdateRequest(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
