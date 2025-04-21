package com.example.harumeonglog.domain.comment.repository;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentReport;
import com.example.harumeonglog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    CommentReport findByCommentAndMember(Comment comment, Member member);

}
