package com.laoumri.service;

import com.laoumri.entity.Comment;
import com.laoumri.entity.Post;
import com.laoumri.response.CommentResponse;

import java.util.List;

public interface CommentService {
    Comment getCommentById(Long commentId);
    Comment createNewComment(String content, Post post);
    Comment updateComment(Long commentId, String content);
    Comment likeComment(Long commentId);
    Comment unlikeComment(Long commentId);
    void deleteComment(Long commentId);
    List<CommentResponse> getPostCommentsPaginate(Post post, Integer page, Integer size);
}
