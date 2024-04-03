package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.controller.dto.WriterDto.WriterSimple;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CommentDto {

    @Getter
    @NoArgsConstructor
    public static class CommentSimple {
        private Long id;
        private WriterSimple writer;
        private LocalDate date;
        private LocalTime time;
        private String content;

        public CommentSimple(Comment comment, Member writer) {
            this.id = comment.getId();
            this.writer = new WriterSimple(writer);
            this.date = comment.getCreatedTime().toLocalDate();
            this.time = comment.getCreatedTime().toLocalTime();
            this.content = comment.getContent();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentInfo {
        private Long id;
        private WriterSimple writer;
        private LocalDate date;
        private LocalTime time;
        private String content;
        private List<CommentInfo> lowerComments;

        @Builder
        public CommentInfo(Long id, Member writer, String content, List<CommentInfo> lowerComments, LocalDate date, LocalTime time) {
            this.id = id;
            this.writer = new WriterSimple(writer);
            this.date = date;
            this.time = time;
            this.content = content;
            this.lowerComments = lowerComments;
        }

        public CommentInfo(Comment comment) {
            this.id = comment.getId();
            this.writer = new WriterSimple(comment.getWriter());
            this.date = comment.getCreatedTime().toLocalDate();
            this.time = comment.getCreatedTime().toLocalTime();
            this.content = comment.getContent();
            if(!comment.getLowerComments().isEmpty()) {
                this.lowerComments = comment.getLowerComments().stream()
                        .map(CommentInfo::new)
                        .collect(Collectors.toList());
            }
        }
    }


    @Getter
    @NoArgsConstructor
    public static class CommentDetail {
        private Long id;
        private Long post;
        private WriterSimple writer;
        private LocalDate date;
        private LocalTime time;
        private Long upperComment;
        private List<CommentDetail> lowerComments;
        private String content;

        @Builder
        public CommentDetail(Long id, Long postId, Member writer, Long upperCommentId, List<CommentDetail> lowerComments, String content,  LocalDate date, LocalTime time) {
            this.id = id;
            this.post = postId;
            this.writer = new WriterSimple(writer);
            this.date = date;
            this.time = time;
            this.upperComment = upperCommentId;
            this.lowerComments = lowerComments;
            this.content = content;
        }

        public CommentDetail(Comment comment) {
            this.id = comment.getId();
            this.post = comment.getPost().getId();
            this.writer = new WriterSimple(comment.getWriter());
            this.date = comment.getCreatedTime().toLocalDate();
            this.time = comment.getCreatedTime().toLocalTime();
            if(comment.getUpperComment() != null) {
                this.upperComment = comment.getUpperComment().getId();
            }
            if(!comment.getLowerComments().isEmpty()) {
                this.lowerComments = comment.getLowerComments().stream()
                        .map(CommentDetail::new)
                        .collect(Collectors.toList());
            }
            this.content = comment.getContent();
        }

        public List<CommentDetail> toList(List<Comment> comments) {
            List<CommentDetail> commentDetailList = new ArrayList<>();
            for (Comment comment : comments) {
                commentDetailList.add(new CommentDetail(comment));
            }
            return commentDetailList;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentSaveRequest {
        private String content;
        private Long upperCommentId;

        @Builder
        public CommentSaveRequest(String content, Long upperComment) {
            this.content = content;
            this.upperCommentId = upperComment;
        }

        public Comment toEntity(Member writer, Post post) {
            return Comment.builder()
                    .writer(writer)
                    .post(post)
                    .content(content)
                    .lowerComments(new ArrayList<Comment>())
                    .deleteYn("N")
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentUpdateRequest {
        private String content;

        @Builder
        public CommentUpdateRequest(String content) {
            this.content = content;
        }

    }
}
