package com.example.honjarang.domain.post.service;


import com.example.honjarang.domain.post.dto.*;
import com.example.honjarang.domain.DateTimeUtils;
import com.example.honjarang.domain.post.entity.Category;
import com.example.honjarang.domain.post.entity.Comment;
import com.example.honjarang.domain.post.entity.LikePost;
import com.example.honjarang.domain.post.entity.Post;
import com.example.honjarang.domain.post.exception.*;
import com.example.honjarang.domain.post.repository.CommentRepository;
import com.example.honjarang.domain.post.repository.LikePostRepository;
import com.example.honjarang.domain.post.repository.PostRepository;
import com.example.honjarang.domain.user.entity.Role;
import com.example.honjarang.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.JSONOutput;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final LikePostRepository likePostRepository;

    private final S3Client s3Client;



    @Transactional
    public Long createPost(PostCreateDto postCreateDto, MultipartFile postImage, User user) throws IOException {

        String uuid = UUID.randomUUID().toString();
        String image = "";
        if (postImage != null) {
            try {
                s3Client.putObject(PutObjectRequest.builder()
                        .bucket("honjarang-bucket")
                        .key("postImage/" + uuid + postImage.getOriginalFilename())
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .contentType(postImage.getContentType())
                        .build(), RequestBody.fromInputStream(postImage.getInputStream(), postImage.getSize()));
            } catch (IOException e) {
                throw new RuntimeException("게시글 이미지 업로드에 실패했습니다.");
            }

            image = uuid + postImage.getOriginalFilename();
        }
        return postRepository.save(postCreateDto.toEntity(user, image)).getId();
    }

    @Transactional
    public void deletePost(long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("존재하지 않는 게시글입니다."));

        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new InvalidUserException("작성자만 삭제할 수 있습니다.");
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public void updatePost(MultipartFile postImage, PostUpdateDto postUpdateDto, User user, Long id) throws IOException {

        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("존재하지 않는 게시글입니다."));
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new InvalidUserException("작성자만 수정할 수 있습니다.");
        }


        // 기존에 게시글에 사진이 있었고 사용자가 사진을 첨부했을 때
        // 기존의 사진을 삭제하고 새로운 사진 추가
        if (post.getPostImage()!= null && postImage!=null) {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("postImage/" + post.getPostImage())
                    .build();
            s3Client.deleteObject(request);

             // 사진추가
            String uuid = UUID.randomUUID().toString();
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("postImage/" + uuid + postImage.getOriginalFilename())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(postImage.getContentType())
                    .build(), RequestBody.fromInputStream(postImage.getInputStream(), postImage.getSize()));

            String image = uuid + postImage.getOriginalFilename();
            post.update(postUpdateDto);
            post.updateImage(image);

        }
        
        // 기존에 게시글에 사진이 있었고 사용자가 사진을 첨부하지 않았을 때
        // 그냥 놔둬야 됨, 아무것도 건들이지 않음
        // 아예 parameter를 없애야 성공적으로 됨
        if (post.getPostImage()!= null && postImage==null) {
            post.update(postUpdateDto);
            post.updateImage(post.getPostImage());
        }


        // 기존에 사진이 없었고 사용자가 사진을 첨부했을 때
        // 사진 추가와 글 업데이트
        if (post.getPostImage()== null && postImage!=null) {
            // 사진추가
            String uuid = UUID.randomUUID().toString();
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket("honjarang-bucket")
                    .key("postImage/" + uuid + postImage.getOriginalFilename())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(postImage.getContentType())
                    .build(), RequestBody.fromInputStream(postImage.getInputStream(), postImage.getSize()));

            String image = uuid + postImage.getOriginalFilename();
            post.update(postUpdateDto);
            post.updateImage(image);
        }

        // 기존에 사진이 없었고 사용자가 사진을 첨부하지 않았을 때
        // 그냥 글만 업데이트 하면됨
        if (post.getPostImage()== null && postImage==null) {
            post.update(postUpdateDto);
            post.updateImage(null);
        }

    }

    @Transactional
    public void createComment(Long id, CommentCreateDto commentCreateDto, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
        if (commentCreateDto.getContent().isEmpty()) {
            throw new ContentEmptyException("댓글을 작성하세요.");
        }
        commentRepository.save(commentCreateDto.toEntity(post, user));
    }


    @Transactional(readOnly = true)
    public List<PostListDto> getPostList(int page, String keyword) {
        Pageable pageable = PageRequest.of(page -1, 15);

        List<Post> posts = postRepository.findAllByTitleContainingIgnoreCaseOrderByIsNoticeDescIdDesc(keyword, pageable).toList();
        List<PostListDto> postListDtos = new ArrayList<>();

        for(Post post : posts){
            Integer likeCnt = likePostRepository.countByPostId(post.getId());
            Integer commentCnt = commentRepository.countByPostId(post.getId());
            postListDtos.add(new PostListDto(post , likeCnt, commentCnt));
        }
        return  postListDtos;
    }

    @Transactional
    public PostDto getPost(long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
        Integer likedCnt = likePostRepository.countByPostId(id);
        int liked = likePostRepository.countByPostIdAndUserId(id,user.getId());
        Boolean isLiked = false;
        postRepository.increaseViews(id);
        post.increaseViews();
        if(liked==1){
            isLiked = true;
        }
        return new PostDto(post, likedCnt, isLiked);

    }

    @Transactional
    public void togglePostLike(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
        if (likePostRepository.countByPostIdAndUserId(id, user.getId()) == 0) {
            likePostRepository.save(LikePost.builder().post(post).user(user).build());
        } else {
            likePostRepository.deleteByPostIdAndUserId(id, user.getId());
        }
    }


    private CommentListDto toCommentListDto(Comment comment) {
        return new CommentListDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                DateTimeUtils.formatLocalDateTime(comment.getCreatedAt())
        );
    }

    @Transactional
    public void deleteComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글 입니다."));
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new InvalidUserException("작성자만 삭제할 수 있습니다.");
        }
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CommentListDto> getCommentList(Long id) {
        postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("게시글이 존재하지 않습니다."));
        return commentRepository.findAllByPostId(id).stream()
                .map(this::toCommentListDto)
                .toList();

    }

    @Transactional(readOnly = true)
    public Integer getPostsPageCount(Integer size, String keyword) {
        return (int) Math.ceil((double) postRepository.countAllByTitleContainingIgnoreCase(keyword) / size) ;
    }

    @Transactional
    public void noticePost(Long id, User user){
        Post post = postRepository.findById(id).orElseThrow(()-> new PostNotFoundException("존재하지 않는 게시글입니다."));
        if(user.getRole()== Role.ROLE_ADMIN) {
            post.updateNotice();
        }else{
            throw new InvalidUserException("관리자만 공지사항을 설정 및 해제할 수 있습니다.");
        }
    }
}
