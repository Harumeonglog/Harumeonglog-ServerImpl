package com.example.harumeonglog.domain.comment.repository;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentBlock;
import com.example.harumeonglog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentBlockRepository extends JpaRepository<CommentBlock, Long> {
    Optional<CommentBlock> findByMemberAndComment(Member member, Comment comment);
}
