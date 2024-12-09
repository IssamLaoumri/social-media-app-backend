package com.laoumri.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoumri.entity.Comment;
import com.laoumri.entity.Post;
import com.laoumri.entity.User;
import com.laoumri.response.CommentResponse;
import com.laoumri.dto.TagDto;
import com.laoumri.entity.Tag;
import com.laoumri.exception.EmptyPostException;
import com.laoumri.response.PostResponse;
import com.laoumri.service.CommentService;
import com.laoumri.service.PostService;
import com.laoumri.service.TagService;
import com.laoumri.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final TagService tagService;

    @PostMapping("/posts/create")
    public ResponseEntity<?> createNewPost(@RequestParam(required = false) Optional<String> content,
                                           @RequestParam(required = false) Optional<MultipartFile> postPhoto,
                                           @RequestParam(required = false) Optional<String> postTags) throws JsonProcessingException {
        if ((content.isEmpty() || content.get().length() <= 0) &&
                (postPhoto.isEmpty() || postPhoto.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }

        ObjectMapper mapper = new ObjectMapper();

        String contentToAdd = content.isEmpty() ? null : content.get();
        MultipartFile postPhotoToAdd = postPhoto.isEmpty() ? null : postPhoto.get();
        List<TagDto> postTagsToAdd = postTags.isEmpty() ? null :
                mapper.readValue(postTags.get(), new TypeReference<>() {});

        Post createdPost = postService.createNewPost(contentToAdd, postPhotoToAdd, postTagsToAdd);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PostMapping("/posts/{postId}/update")
    public ResponseEntity<?> updatePost(@PathVariable Long postId,
                                        @RequestParam(required = false) Optional<String> content,
                                        @RequestParam(required = false) Optional<MultipartFile> postPhoto,
                                        @RequestParam(required = false) Optional<String> postTags) throws JsonProcessingException {
        if ((content.isEmpty() || content.get().length() <= 0) &&
                (postPhoto.isEmpty() || postPhoto.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }

        ObjectMapper mapper = new ObjectMapper();

        String contentToAdd = content.isEmpty() ? null : content.get();
        MultipartFile postImageToAdd = postPhoto.isEmpty() ? null : postPhoto.get();
        List<TagDto> postTagsToAdd = postTags.isEmpty() ? null :
                mapper.readValue(postTags.get(), new TypeReference<>() {});

        Post updatePost = postService.updatePost(postId, contentToAdd, postImageToAdd, postTagsToAdd);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/photo/delete")
    public ResponseEntity<?> deletePostPhoto(@PathVariable Long postId) {
        postService.deletePostPhoto(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        PostResponse foundPostResponse = postService.getPostResponseById(postId);
        return new ResponseEntity<>(foundPostResponse, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable Long postId,
                                          @RequestParam Integer page,
                                          @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post targetPost = postService.getPostById(postId);
        List<User> postLikerList = userService.getLikesByPostPaginate(targetPost, page, size);
        return new ResponseEntity<>(postLikerList, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/shares")
    public ResponseEntity<?> getPostShares(@PathVariable Long postId,
                                           @RequestParam Integer page,
                                           @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post sharedPost = postService.getPostById(postId);
        List<PostResponse> foundPostShares = postService.getPostSharesPaginate(sharedPost, page, size);
        return new ResponseEntity<>(foundPostShares, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId) {
        postService.unlikePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId,
                                             @RequestParam Integer page,
                                             @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post targetPost = postService.getPostById(postId);
        List<CommentResponse> postCommentResponseList = commentService.getPostCommentsPaginate(targetPost, page, size);
        return new ResponseEntity<>(postCommentResponseList, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/comments/create")
    public ResponseEntity<?> createPostComment(@PathVariable Long postId,
                                               @RequestParam String content) {
        Comment savedComment = postService.createPostComment(postId, content);
        CommentResponse commentResponse = CommentResponse.builder()
                .comment(savedComment)
                .likedByAuthUser(false)
                .build();
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/update")
    public ResponseEntity<?> updatePostComment(@PathVariable Long commentId,
                                               @PathVariable Long postId,
                                               @RequestParam String content) {
        Comment savedComment = postService.updatePostComment(commentId, postId, content);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public ResponseEntity<?> deletePostComment(@PathVariable Long commentId,
                                               @PathVariable Long postId) {
        postService.deletePostComment(commentId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/posts/comments/{commentId}/like")
    public ResponseEntity<?> likePostComment(@PathVariable Long commentId) {
        commentService.likeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/posts/comments/{commentId}/unlike")
    public ResponseEntity<?> unlikePostComment(@PathVariable Long commentId) {
        commentService.unlikeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts/comments/{commentId}/likes")
    public ResponseEntity<?> getCommentLikeList(@PathVariable Long commentId,
                                                @RequestParam Integer page,
                                                @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Comment targetComment = commentService.getCommentById(commentId);
        List<User> commentLikes = userService.getLikesByCommentPaginate(targetComment, page, size);
        return new ResponseEntity<>(commentLikes, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/share/create")
    public ResponseEntity<?> createPostShare(@PathVariable Long postId,
                                             @RequestParam(required = false) Optional<String> content) {
        String contentToAdd = content.isEmpty() ? null : content.get();
        Post postShare = postService.createPostShare(contentToAdd, postId);
        return new ResponseEntity<>(postShare, HttpStatus.OK);
    }

    @PostMapping("/posts/{postShareId}/share/update")
    public ResponseEntity<?> updatePostShare(@PathVariable Long postShareId,
                                             @RequestParam(required = false) Optional<String> content) {
        String contentToAdd = content.isEmpty() ? null : content.get();
        Post updatedPostShare = postService.updatePostShare(contentToAdd, postShareId);
        return new ResponseEntity<>(updatedPostShare, HttpStatus.OK);
    }

    @PostMapping("/posts/{postShareId}/share/delete")
    public ResponseEntity<?> deletePostShare(@PathVariable Long postShareId) {
        postService.deletePostShare(postShareId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts/tags/{tagName}")
    public ResponseEntity<?> getPostsByTag(@PathVariable String tagName,
                                          @RequestParam Integer page,
                                          @RequestParam Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Tag targetTag = tagService.getTagByName(tagName);
        List<PostResponse> taggedPosts = postService.getPostByTagPaginate(targetTag, page, size);
        return new ResponseEntity<>(taggedPosts, HttpStatus.OK);
    }
}
