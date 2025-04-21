package com.example.harumeonglog.domain.comment.converter;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentReport;
import com.example.harumeonglog.domain.member.entity.Member;

public class CommentReportConverter {

    public static CommentReport toCommentReport(Comment comment, Member member) {
        return CommentReport.builder()
                .comment(comment)
                .member(member)
                .build();
    }
}
