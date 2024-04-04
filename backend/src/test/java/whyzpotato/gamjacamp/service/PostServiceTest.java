package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostDetail;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostSaveRequest;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostSimple;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostUpdateRequest;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostDetail;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSaveRequest;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSimple;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostUpdateRequest;
import whyzpotato.gamjacamp.repository.ImageRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private PostService postService;

    @Test
    void 자유게시글_작성_이미지여러개() {
        Member member = Member.builder().build();
        Image image = Image.builder().build();
        Image image1 = Image.builder().build();
        Image image2 = Image.builder().build();
        Post post = Post.builder().writer(member).images(new ArrayList<>()).build();
        GeneralPostSaveRequest request = GeneralPostSaveRequest.builder().title("제목").content("내용").build();
        List<String> fileNameList = Stream.of("test.jpg", "test1.jpg", "test2.jpg").collect(toList());
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.save(any())).willReturn(post);
        given(imageRepository.save(any())).willReturn(image, image1, image2);

        Long postId = postService.saveGeneralPost(member.getId(), request, fileNameList);

        assertThat(postId).isEqualTo(post.getId());
        assertThat(post.getImages()).hasSize(3);
        assertThat(post.getImages()).contains(image, image1, image2);
    }

    @Test
    void 자유게시글_작성_이미지한개() {
        Member member = Member.builder().build();
        Image image = Image.builder().build();
        Post post = Post.builder().writer(member).images(new ArrayList<>()).build();
        GeneralPostSaveRequest request = GeneralPostSaveRequest.builder().title("제목").content("내용").build();
        List<String> fileNameList = Stream.of("test.jpg").collect(toList());
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.save(any())).willReturn(post);
        given(imageRepository.save(any())).willReturn(image);

        Long postId = postService.saveGeneralPost(member.getId(), request, fileNameList);

        assertThat(postId).isEqualTo(post.getId());
        assertThat(post.getImages()).hasSize(1);
        assertThat(post.getImages()).contains(image);
    }

    @Test
    void 자유게시글_작성_이미지없음() {
        Member member = Member.builder().build();
        Post post = Post.builder().writer(member).images(new ArrayList<>()).build();
        GeneralPostSaveRequest request = GeneralPostSaveRequest.builder().title("제목").content("내용").build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.save(any())).willReturn(post);

        Long postId = postService.saveGeneralPost(member.getId(), request, null);

        assertThat(postId).isEqualTo(post.getId());
        assertThat(post.getImages()).isEmpty();
    }

    @Test
    void 자유게시글_조회() {
        Member member = Member.builder().username("작성자").picture("https://picsum.photos/300/300").build();
        Post post = Post.builder().writer(member).title("제목").content("내용").type(PostType.GENERAL).images(new ArrayList<>()).build();
        Post spyPost = spy(post);
        given(spyPost.getCreatedTime()).willReturn(LocalDateTime.now());
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(spyPost));

        GeneralPostDetail generalPostDto = postService.findGeneralPost(spyPost.getId());

        assertThat(generalPostDto.getId()).isEqualTo(spyPost.getId());
        assertThat(generalPostDto.getWriter().getId()).isEqualTo(member.getId());
        assertThat(generalPostDto.getWriter().getUsername()).isEqualTo(member.getUsername());
        assertThat(generalPostDto.getWriter().getProfile()).isEqualTo(member.getPicture());
        assertThat(generalPostDto.getTitle()).isEqualTo("제목");
        assertThat(generalPostDto.getContent()).isEqualTo("내용");
        assertThat(generalPostDto.getPostType()).isEqualTo(PostType.GENERAL);
        assertThat(generalPostDto.getImages()).isNull();
    }

    @Test
    void 자유게시판목록_없음() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> posts = Page.empty(pageable);
        given(postRepository.findByIdLessThanAndType(anyLong(), eq(PostType.GENERAL), any())).willReturn(posts);

        Page<GeneralPostSimple> generalPostSimples = postService.findGeneralPostList(100L);
        assertThat(generalPostSimples.getContent()).isEmpty();
    }

    @Test
    void 자유게시판목록_이미지없음() {
        Post spyPost1 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("제목").content("내용").images(new ArrayList<>()).build());
        Post spyPost2 = spy(Post.builder().writer(Member.builder().build()).images(new ArrayList<>()).build());
        given(spyPost1.getCreatedTime()).willReturn(LocalDateTime.now());
        given(spyPost2.getCreatedTime()).willReturn(LocalDateTime.now());
        List<Post> postList = new ArrayList<>();
        postList.add(spyPost1);
        postList.add(spyPost2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> postPage = new PageImpl<>(postList, pageable, 2);
        given(postRepository.findByIdLessThanAndType(anyLong(), eq(PostType.GENERAL), any(Pageable.class))).willReturn(postPage);

        Page<GeneralPostSimple> posts = postService.findGeneralPostList(100L);

        assertThat(posts.getTotalElements()).isEqualTo(2);
        assertThat(posts.getNumber()).isEqualTo(0);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.isFirst()).isTrue();
        assertThat(posts.hasNext()).isFalse();
        assertThat(posts.getContent().get(0).getWriter().getUsername()).isEqualTo("작성자");
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("제목");
        assertThat(posts.getContent().get(0).getContent()).isEqualTo("내용");
        assertThat(posts.getContent().get(0).getImage()).isNull();
    }

    @Test
    void 자유게시판목록_이미지있음() {
        List<Image> images = new ArrayList<>();
        images.add(Image.builder().path("https://picsum.photos/300/300").build());
        images.add(Image.builder().path("https://picsum.photos/400/400").build());
        Post spyPost1 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("제목").content("내용").images(images).build());
        Post spyPost2 = spy(Post.builder().writer(Member.builder().build()).images(new ArrayList<>()).build());
        given(spyPost1.getCreatedTime()).willReturn(LocalDateTime.now());
        given(spyPost2.getCreatedTime()).willReturn(LocalDateTime.now());
        List<Post> postList = new ArrayList<>();
        postList.add(spyPost1);
        postList.add(spyPost2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> postPage = new PageImpl<>(postList, pageable, 2);
        given(postRepository.findByIdLessThanAndType(anyLong(), eq(PostType.GENERAL), any(Pageable.class))).willReturn(postPage);

        Page<GeneralPostSimple> posts = postService.findGeneralPostList(100L);

        assertThat(posts.getTotalElements()).isEqualTo(2);
        assertThat(posts.getNumber()).isEqualTo(0);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.isFirst()).isTrue();
        assertThat(posts.hasNext()).isFalse();
        assertThat(posts.getContent().get(0).getImage()).isEqualTo("https://picsum.photos/300/300");
        assertThat(posts.getContent().get(0).getWriter().getUsername()).isEqualTo("작성자");
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("제목");
        assertThat(posts.getContent().get(0).getContent()).isEqualTo("내용");
        assertThat(posts.getContent().get(1).getImage()).isNull();
    }

    @Test
    void 자유게시글_수정() {
        Member member = Member.builder().username("작성자").picture("https://picsum.photos/300/300").build();
        Post post = Post.builder().writer(member).title("제목").type(PostType.GENERAL).images(new ArrayList<>()).build();
        Post spyPost = spy(post);
        given(spyPost.getCreatedTime()).willReturn(LocalDateTime.now());
        Image image1 = Image.builder().path("https://picsum.photos/700/700").build();
        Image image2 = Image.builder().path("https://picsum.photos/800/800").build();
        GeneralPostUpdateRequest request = GeneralPostUpdateRequest.builder().title("수정된제목").content("수정된내용").build();
        List<String> fileNameList = Stream.of("updated1.jpg","updated2.jpg").collect(toList());
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(spyPost));
        given(imageRepository.save(any(Image.class))).willReturn(image1, image2);
        given(postRepository.save(spyPost.update(request))).willReturn(spyPost);

        GeneralPostDetail generalPostDetail = postService.updateGeneralPost(member.getId(), post.getId(), request, fileNameList);

        assertThat(generalPostDetail.getId()).isEqualTo(post.getId());
        assertThat(generalPostDetail.getWriter().getUsername()).isEqualTo("작성자");
        assertThat(generalPostDetail.getWriter().getProfile()).isEqualTo("https://picsum.photos/300/300");
        assertThat(generalPostDetail.getTitle()).isEqualTo("수정된제목");
        assertThat(generalPostDetail.getContent()).isEqualTo("수정된내용");
        assertThat(generalPostDetail.getPostType()).isEqualTo(PostType.GENERAL);
        assertThat(generalPostDetail.getImages()).hasSize(2);
        assertThat(generalPostDetail.getImages()).contains("https://picsum.photos/700/700","https://picsum.photos/800/800");
    }

    @Test
    void 자유게시글_삭제_성공() {
        Member member = Member.builder().build();
        Post post = Post.builder().writer(member).build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        doNothing().when(postRepository).delete(eq(post));

        assertThatCode(() -> postService.deleteGeneralPost(member.getId(), post.getId())).doesNotThrowAnyException();
    }

    @Test
    void 자유게시글_삭제_실패() {
        Member member = Member.builder().build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> postService.deleteGeneralPost(member.getId(), 1L)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 자유게시판_검색() {
        Post spyPost1 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("KEY").content("내용").images(new ArrayList<>()).build());
        Post spyPost2 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("제목").content("KEY").images(new ArrayList<>()).build());
        given(spyPost1.getCreatedTime()).willReturn(LocalDateTime.now());
        given(spyPost2.getCreatedTime()).willReturn(LocalDateTime.now());
        List<Post> postList = new ArrayList<>();
        postList.add(spyPost1);
        postList.add(spyPost2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> postPage = new PageImpl<>(postList, pageable, 2);
        given(postRepository.findByTitleOrContentContainingAndIdLessThanAndType(matches("KEY"), matches("KEY"), anyLong(), eq(PostType.GENERAL), any(Pageable.class))).willReturn(postPage);

        Page<GeneralPostSimple> posts = postService.searchGeneralPost(100L, "KEY");

        assertThat(posts.getTotalElements()).isEqualTo(2);
        assertThat(posts.getNumber()).isEqualTo(0);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.isFirst()).isTrue();
        assertThat(posts.hasNext()).isFalse();
        assertThat(posts.getContent().get(0).getWriter().getUsername()).isEqualTo("작성자");
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("KEY");
        assertThat(posts.getContent().get(1).getContent()).isEqualTo("KEY");
    }

    @Test
    void 모집게시글_작성_이미지여러개() {
        Member member = Member.builder().build();
        Image image1 = Image.builder().build();
        Image image2 = Image.builder().build();
        Image image3 = Image.builder().build();
        Post post = Post.builder().writer(member).images(new ArrayList<>()).build();
        GatherPostSaveRequest request = GatherPostSaveRequest.builder().title("제목").content("내용").build();
        List<String> fileNameList = Stream.of("test1.jpg", "test2.jpg", "test3.jpg").collect(toList());
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.save(any(Post.class))).willReturn(post);
        given(imageRepository.save(any(Image.class))).willReturn(image1, image2, image3);

        Long postId = postService.saveGatherPost(member.getId(), request, fileNameList);

        assertThat(postId).isEqualTo(post.getId());
        assertThat(post.getImages()).hasSize(3);
        assertThat(post.getImages()).contains(image1, image2, image3);
    }

    @Test
    void 모집게시글_작성_이미지한개() {
        Member member = Member.builder().build();
        Image image = Image.builder().build();
        Post post = Post.builder().writer(member).images(new ArrayList<>()).build();
        GatherPostSaveRequest request = GatherPostSaveRequest.builder().title("제목").content("내용").build();
        List<String> fileNameList = Stream.of("test.jpg").collect(toList());
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.save(any(Post.class))).willReturn(post);
        given(imageRepository.save(any(Image.class))).willReturn(image);

        Long postId = postService.saveGatherPost(member.getId(), request, fileNameList);

        assertThat(postId).isEqualTo(post.getId());
        assertThat(post.getImages()).hasSize(1);
        assertThat(post.getImages()).contains(image);
    }

    @Test
    void 모집게시글_작성_이미지없음() {
        Member member = Member.builder().build();
        Post post = Post.builder().writer(member).images(new ArrayList<>()).build();
        GatherPostSaveRequest request = GatherPostSaveRequest.builder().title("제목").content("내용").build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.save(any(Post.class))).willReturn(post);

        Long postId = postService.saveGatherPost(member.getId(), request, null);
        assertThat(postId).isEqualTo(post.getId());
        assertThat(post.getImages()).isEmpty();
    }

    @Test
    void 모집게시글_조회(){
        Member member = Member.builder().username("작성자").picture("https://picsum.photos/300/300").build();
        LocalDateTime dateTime = LocalDateTime.now();
        Post post = Post.builder().writer(member).title("제목").content("내용").type(PostType.GATHER).images(new ArrayList<>()).build();
        Post spyPost = spy(post);
        given(spyPost.getCreatedTime()).willReturn(dateTime);
        given(postRepository.findById(spyPost.getId())).willReturn(Optional.ofNullable(spyPost));

        GatherPostDetail postDto = postService.findGatherPost(spyPost.getId());

        assertThat(postDto.getId()).isEqualTo(spyPost.getId());
        assertThat(postDto.getWriter().getId()).isEqualTo(member.getId());
        assertThat(postDto.getWriter().getUsername()).isEqualTo("작성자");
        assertThat(postDto.getWriter().getProfile()).isEqualTo("https://picsum.photos/300/300");
        assertThat(postDto.getDate()).isEqualTo(dateTime.toLocalDate());
        assertThat(postDto.getTime()).isEqualTo(dateTime.toLocalTime());
        assertThat(postDto.getTitle()).isEqualTo("제목");
        assertThat(postDto.getContent()).isEqualTo("내용");
        assertThat(postDto.getPostType()).isEqualTo(PostType.GATHER);
        assertThat(postDto.getImages()).isNull();
    }

    @Test
    void 모집게시글_목록조회_없음() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> posts = Page.empty(pageable);
        given(postRepository.findByIdLessThanAndType(anyLong(), eq(PostType.GATHER), any())).willReturn(posts);

        Page<GatherPostSimple> gatherPostSimples = postService.findGatherPostList(100L);

        assertThat(gatherPostSimples.getContent()).isEmpty();
    }

    @Test
    void 모집게시글_목록조회_이미지없음() {
        LocalDateTime dateTime = LocalDateTime.now();
        Post spyPost1 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("제목").content("내용").images(new ArrayList<>()).build());
        Post spyPost2 = spy(Post.builder().writer(Member.builder().build()).images(new ArrayList<>()).build());
        given(spyPost1.getCreatedTime()).willReturn(dateTime);
        given(spyPost2.getCreatedTime()).willReturn(dateTime);
        List<Post> postList = new ArrayList<>();
        postList.add(spyPost1);
        postList.add(spyPost2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> postPage = new PageImpl<>(postList, pageable, 2);
        given(postRepository.findByIdLessThanAndType(anyLong(), eq(PostType.GATHER), any(Pageable.class))).willReturn(postPage);

        Page<GatherPostSimple> posts = postService.findGatherPostList(100L);

        assertThat(posts.getTotalElements()).isEqualTo(2);
        assertThat(posts.getNumber()).isEqualTo(0);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.isFirst()).isTrue();
        assertThat(posts.hasNext()).isFalse();
        assertThat(posts.getContent().get(0).getWriter().getUsername()).isEqualTo("작성자");
        assertThat(posts.getContent().get(0).getDate()).isEqualTo(dateTime.toLocalDate());
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("제목");
        assertThat(posts.getContent().get(0).getContent()).isEqualTo("내용");
        assertThat(posts.getContent().get(0).getImage()).isNull();
    }

    @Test
    void 모집게시글_목록조회_이미지있음() {
        List<Image> images = new ArrayList<>();
        images.add(Image.builder().path("https://picsum.photos/300/300").build());
        images.add(Image.builder().path("https://picsum.photos/400/400").build());
        LocalDateTime dateTime = LocalDateTime.now();
        Post spyPost1 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("제목").content("내용").images(images).build());
        Post spyPost2 = spy(Post.builder().writer(Member.builder().build()).images(new ArrayList<>()).build());
        given(spyPost1.getCreatedTime()).willReturn(dateTime);
        given(spyPost2.getCreatedTime()).willReturn(dateTime);
        List<Post> postList = new ArrayList<>();
        postList.add(spyPost1);
        postList.add(spyPost2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> postPage = new PageImpl<>(postList, pageable, 2);
        given(postRepository.findByIdLessThanAndType(anyLong(), eq(PostType.GATHER), any(Pageable.class))).willReturn(postPage);

        Page<GatherPostSimple> posts = postService.findGatherPostList(100L);

        assertThat(posts.getTotalElements()).isEqualTo(2);
        assertThat(posts.getNumber()).isEqualTo(0);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.isFirst()).isTrue();
        assertThat(posts.hasNext()).isFalse();
        assertThat(posts.getContent().get(0).getWriter().getUsername()).isEqualTo("작성자");
        assertThat(posts.getContent().get(0).getDate()).isEqualTo(dateTime.toLocalDate());
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("제목");
        assertThat(posts.getContent().get(0).getContent()).isEqualTo("내용");
        assertThat(posts.getContent().get(0).getImage()).isEqualTo("https://picsum.photos/300/300");
        assertThat(posts.getContent().get(1).getImage()).isNull();
    }

    @Test
    void 모집게시글_수정() {
        LocalDateTime dateTime = LocalDateTime.now();
        Member member = Member.builder().username("작성자").picture("https://picsum.photos/300/300").build();
        Post post = Post.builder().writer(member).title("제목").type(PostType.GATHER).images(new ArrayList<>()).build();
        Post spyPost = spy(post);
        given(spyPost.getCreatedTime()).willReturn(dateTime);
        Image image1 = Image.builder().path("https://picsum.photos/700/700").build();
        Image image2 = Image.builder().path("https://picsum.photos/800/800").build();
        GatherPostUpdateRequest request = GatherPostUpdateRequest.builder().title("수정된제목").content("수정된내용").build();
        List<String> fileNameList = Stream.of("updated1.jpg","updated2.jpg").collect(toList());
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(spyPost));
        given(imageRepository.save(any(Image.class))).willReturn(image1, image2);
        given(postRepository.save(spyPost.update(request))).willReturn(spyPost);

        GatherPostDetail postDto = postService.updateGatherPost(member.getId(), spyPost.getId(), request, fileNameList);

        assertThat(postDto.getId()).isEqualTo(spyPost.getId());
        assertThat(postDto.getWriter().getUsername()).isEqualTo("작성자");
        assertThat(postDto.getWriter().getProfile()).isEqualTo("https://picsum.photos/300/300");
        assertThat(postDto.getTitle()).isEqualTo("수정된제목");
        assertThat(postDto.getContent()).isEqualTo("수정된내용");
        assertThat(postDto.getDate()).isEqualTo(dateTime.toLocalDate());
        assertThat(postDto.getPostType()).isEqualTo(PostType.GATHER);
        assertThat(postDto.getImages()).hasSize(2);
        assertThat(postDto.getImages()).contains("https://picsum.photos/700/700","https://picsum.photos/800/800");

    }

    @Test
    void 모집게시글_삭제_성공() {
        Member member = Member.builder().build();
        Post post = Post.builder().writer(member).build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));
        doNothing().when(postRepository).delete(eq(post));

        assertThatCode(()->postService.deleteGatherPost(member.getId(), post.getId())).doesNotThrowAnyException();
    }

    @Test
    void 모집게시글_삭제_실패() {
        Member member = Member.builder().build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(()->postService.deleteGatherPost(member.getId(), 1L)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 모집게시글_검색() {
        LocalDateTime dateTime = LocalDateTime.now();
        Post spyPost1 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("KEY").content("내용").images(new ArrayList<>()).build());
        Post spyPost2 = spy(Post.builder().writer(Member.builder().username("작성자").build()).title("제목").content("KEY").images(new ArrayList<>()).build());
        given(spyPost1.getCreatedTime()).willReturn(dateTime);
        given(spyPost2.getCreatedTime()).willReturn(dateTime);
        List<Post> postList = new ArrayList<>();
        postList.add(spyPost1);
        postList.add(spyPost2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> postPage = new PageImpl<>(postList, pageable, 2);
        given(postRepository.findByTitleOrContentContainingAndIdLessThanAndType(matches("KEY"), matches("KEY"), anyLong(), eq(PostType.GATHER), any(Pageable.class))).willReturn(postPage);

        Page<GatherPostSimple> posts = postService.searchGatherPost(100L, "KEY");

        assertThat(posts.getTotalElements()).isEqualTo(2);
        assertThat(posts.getNumber()).isEqualTo(0);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.isFirst()).isTrue();
        assertThat(posts.hasNext()).isFalse();
        assertThat(posts.getContent().get(0).getWriter().getUsername()).isEqualTo("작성자");
        assertThat(posts.getContent().get(0).getDate()).isEqualTo(dateTime.toLocalDate());
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("KEY");
        assertThat(posts.getContent().get(1).getContent()).isEqualTo("KEY");
    }

}
