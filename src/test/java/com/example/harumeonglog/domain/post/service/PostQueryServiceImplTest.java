package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse.MemberInfoResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.dto.response.PostResponse.PostDetailResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.util.S3Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "LOCAL_DB_URL=jdbc:h2:mem:testdb",
        "LOCAL_DB_USERNAME=sa",
        "LOCAL_DB_PW=",
        "JWT_SECRET=testjwttestjwttestjwttestjwttestjwttestjwt"
})
class PostQueryServiceImplTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private S3Util s3Util;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private final String TEST_EMAIL = "example@example.com";
    private final String TEST_NICKNAME = "example";
    private final String TEST_PROVIDERID = "example";
    private final SocialType TEST_SOCIALTYPE = SocialType.KAKAO;

    @BeforeEach
    void setup() {
        this.member = memberRepository.save(Member.builder()
                .email(this.TEST_EMAIL)
                .nickname(this.TEST_NICKNAME)
                .providerId(this.TEST_PROVIDERID)
                .socialType(this.TEST_SOCIALTYPE)
                .build());
    }

    @AfterEach
    void clear() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물 검색이 정상 작동하는가")
    void getPostsTest() {
        // given
        PostCategory[] categories = PostCategory.values();

        IntStream.range(0, 100).forEach(i -> {
            PostImage image = PostImage.builder()
                    .postImageKeyName("testImage" + i)
                    .build();

            Post post = Post.builder()
                    .title("post" + i + "title")
                    .content("post" + i + "content")
                    .member(member)
                    .category(categories[i / 20])
                    .build();

            post.addPostImage(image);
            postRepository.save(post);
        });

        Long cursor = 0L;
        Integer size = 20;
        String name = "post";
        PostRequestCategory postCategory = PostRequestCategory.ETC;

        // when
        PostResponse.PostPreviewListResponse response =
                postQueryService.getPosts(cursor, size, name, postCategory, member);

        // then
        assertEquals(size, response.getItems().size());
        assertTrue(response.getItems().stream()
                .allMatch(p -> p.getPostCategory().name().equals(postCategory.name())));
        assertTrue(response.getItems().stream()
                .allMatch(p -> p.getContent().contains(name) || p.getTitle().contains(name)));
    }

    @Test
    @DisplayName("게시물 단건 조회 잘 되는가")
    void getPostsDefaultTest() {
        // given
        PostImage postImage = PostImage.builder()
                .postImageKeyName("testImage")
                .build();

        Post post = Post.builder()
                    .title("post")
                    .content("content")
                    .member(member)
                    .category(PostCategory.INFO)
                    .build();

       post.addPostImage(postImage);
        Post savedPost = postRepository.save(post);

        // when
        PostDetailResponse postDetailResponse = postQueryService.getPost(member, savedPost.getId());

        // then

        // post
        assertThat(postDetailResponse.getPostId()).isEqualTo(savedPost.getId());
        assertThat(postDetailResponse.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(postDetailResponse.getContent()).isEqualTo(savedPost.getContent());
        assertThat(postDetailResponse.getIsLiked()).isFalse();
        assertThat(postDetailResponse.getPostCategory()).isEqualTo(savedPost.getCategory());
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(savedPost.getCreatedAt());
        assertThat(postDetailResponse.getCommentNum()).isEqualTo(savedPost.getCommentNum());
        assertThat(postDetailResponse.getLikeNum()).isEqualTo(savedPost.getPostLikeNum());
        assertThat(post.getPostImageList()).extracting("postImageKeyName")
                .containsExactly("testImage");

        // member
        MemberInfoResponse memberInfoResponse = postDetailResponse.getMemberInfoResponse();
        assertThat(memberInfoResponse.getMemberId()).isEqualTo(member.getId());
        assertThat(memberInfoResponse.getEmail()).isEqualTo(member.getEmail());
        assertThat(memberInfoResponse.getNickname()).isEqualTo(member.getNickname());
        assertThat(memberInfoResponse.getImage()).isEqualTo(member.getImage());
    }


}