package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentDetail;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CommentDto.CommentUpdateRequest;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Comment;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.repository.CommentRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    void 댓글_작성() {
        Member member = spy(Member.builder().build());
        given(member.getId()).willReturn(1L);
        Post post = spy(Post.builder().comments(new ArrayList<>()).build());
        given(post.getId()).willReturn(1L);
        Comment comment = spy(Comment.builder().build());
        given(comment.getId()).willReturn(1L);
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        CommentSaveRequest request = CommentSaveRequest.builder().content("댓글").upperComment(null).build();

        Long commentId = commentService.save(member.getId(), post.getId(), request);

        assertThat(commentId).isEqualTo(comment.getId());
        assertThat(post.getComments().size()).isEqualTo(1);
    }

    @Test
    void 대댓글_작성() {
        Member member = spy(Member.builder().build());
        given(member.getId()).willReturn(1L);
        Comment upperComment = spy(Comment.builder().lowerComments(new ArrayList<>()).build());
        given(upperComment.getId()).willReturn(1L);
        List<Comment> comments = new ArrayList<>();
        comments.add(upperComment);
        Post post = spy(Post.builder().comments(comments).build());
        given(post.getId()).willReturn(1L);
        Comment comment = spy(Comment.builder().build());
        given(comment.getId()).willReturn(2L);
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        given(commentRepository.findById(upperComment.getId())).willReturn((Optional.ofNullable(upperComment)));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        CommentSaveRequest request = CommentSaveRequest.builder().content("대댓글").upperComment(upperComment.getId()).build();

        Long commentId = commentService.save(member.getId(), post.getId(), request);

        assertThat(commentId).isEqualTo(comment.getId());
        assertThat(post.getComments().size()).isEqualTo(2);
        assertThat(upperComment.getLowerComments().size()).isEqualTo(1);
    }

    @Test
    void 댓글_수정() {
        Member member = spy(Member.builder().username("작성자").picture("https://picsum.photos/300/300").build());
        given(member.getId()).willReturn(1L);
        Post post = spy(Post.builder().build());
        given(post.getId()).willReturn(5L);
        LocalDateTime dateTime = LocalDateTime.now();
        Comment comment = spy(Comment.builder().post(post).writer(member).content("댓글").lowerComments(new ArrayList<>()).build());
        given(comment.getId()).willReturn(2L);
        given(comment.getCreatedTime()).willReturn(dateTime);
        CommentUpdateRequest request = CommentUpdateRequest.builder().content("수정된내용").build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.ofNullable(comment));
        given(commentRepository.save(comment.update(request))).willReturn(comment);

        CommentDetail commentDto = commentService.update(member.getId(), comment.getId(), request);

        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getPost()).isEqualTo(5L);
        assertThat(commentDto.getWriter().getId()).isEqualTo(member.getId());
        assertThat(commentDto.getWriter().getUsername()).isEqualTo(member.getUsername());
        assertThat(commentDto.getWriter().getProfile()).isEqualTo(member.getPicture());
        assertThat(commentDto.getDate()).isEqualTo(dateTime.toLocalDate());
        assertThat(commentDto.getTime()).isEqualTo(dateTime.toLocalTime());
        assertThat(commentDto.getContent()).isEqualTo("수정된내용");
    }

    @Test
    void 댓글_삭제_실패() {
        Member writer = spy(Member.builder().build());
        given(writer.getId()).willReturn(1L);
        Post post = spy(Post.builder().build());
        given(post.getId()).willReturn(5L);
        Comment comment = spy(Comment.builder().writer(writer).post(post).build());
        given(comment.getId()).willReturn(1L);
        Member member = spy(Member.builder().build());
        given(member.getId()).willReturn(100L);
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.ofNullable(comment));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));

        assertThatThrownBy(()->commentService.delete(member.getId(), comment.getId())).isInstanceOf(NoSuchElementException.class);
    }


    @Test
    void 댓글_대댓글없음_삭제_성공() {
        Member member = spy(Member.builder().build());
        given(member.getId()).willReturn(1L);
        Post post = spy(Post.builder().comments(new ArrayList<>()).build());
        given(post.getId()).willReturn(5L);
        Comment comment = spy(Comment.builder().writer(member).post(post).lowerComments(new ArrayList<>()).build());
        given(comment.getId()).willReturn(1L);
        post.getComments().add(comment);

        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.ofNullable(comment));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        doNothing().when(commentRepository).delete(eq(comment));

        assertThatCode(()->commentService.delete(member.getId(), comment.getId())).doesNotThrowAnyException();
    }

    @Test
    void 댓글_대댓글있음_삭제_성공() {
        Member member = spy(Member.builder().build());
        given(member.getId()).willReturn(1L);
        Post post = spy(Post.builder().comments(new ArrayList<>()).build());
        given(post.getId()).willReturn(5L);
        Comment comment = spy(Comment.builder().writer(member).post(post).lowerComments(new ArrayList<>()).build());
        given(comment.getId()).willReturn(1L);
        Comment lowerComment1 = Comment.builder().upperComment(comment).build();
        Comment lowerComment2 = Comment.builder().upperComment(comment).build();
        post.getComments().add(comment);
        post.getComments().add(lowerComment1);
        post.getComments().add(lowerComment2);
        comment.getLowerComments().add(lowerComment1);
        comment.getLowerComments().add(lowerComment2);
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.ofNullable(comment));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        doNothing().when(commentRepository).deleteAll(any());
        doNothing().when(commentRepository).delete(eq(comment));

        assertThatCode(()->commentService.delete(member.getId(), comment.getId())).doesNotThrowAnyException();
    }

    @Test
    void 대댓글_삭제_성공() {
        Member member = spy(Member.builder().build());
        given(member.getId()).willReturn(1L);
        Post post = spy(Post.builder().comments(new ArrayList<>()).build());
        given(post.getId()).willReturn(5L);
        Comment upperComment = Comment.builder().lowerComments(new ArrayList<>()).build();
        Comment comment = spy(Comment.builder().writer(member).post(post).upperComment(upperComment).lowerComments(new ArrayList<>()).build());
        given(comment.getId()).willReturn(2L);
        post.getComments().add(upperComment);
        post.getComments().add(comment);
        upperComment.getLowerComments().add(comment);
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.ofNullable(comment));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        doNothing().when(commentRepository).delete(eq(comment));

        assertThatCode(() -> commentService.delete(member.getId(), comment.getId())).doesNotThrowAnyException();
    }

}
