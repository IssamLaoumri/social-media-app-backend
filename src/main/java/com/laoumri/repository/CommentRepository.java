package com.laoumri.repository;

import com.laoumri.entity.Comment;
import com.laoumri.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post, Pageable pageable);
}
