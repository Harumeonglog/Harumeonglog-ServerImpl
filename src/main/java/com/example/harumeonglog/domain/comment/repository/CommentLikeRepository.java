package com.example.harumeonglog.domain.comment.repository;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentLike;
import com.example.harumeonglog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    CommentLike findByCommentAndMember(Comment comment, Member member);

}
