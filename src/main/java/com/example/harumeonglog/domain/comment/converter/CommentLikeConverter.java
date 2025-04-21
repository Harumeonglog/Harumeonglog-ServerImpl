package com.example.harumeonglog.domain.comment.converter;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.domain.comment.entity.CommentLike;
import com.example.harumeonglog.domain.member.entity.Member;

public class CommentLikeConverter {

    public static CommentLike toCommentLike(Comment comment, Member member) {
        return CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();
    }
}
