package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.controller.port.NoticeService;
import com.example.harumeonglog.domain.member.domain.Notice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;

import static org.junit.jupiter.api.Assertions.*;

class NoticeServiceImplTest {

    private NoticeService noticeService;

    @BeforeEach
    void init() {
        noticeService = NoticeServiceImpl.builder().build();
    }

    @Test
    void canGetNotices() {
        //given
        Integer size = 2;
        Long cursor = 0L;

        //when
        Slice<Notice> notices = noticeService.getNotices(size, cursor);

        //then

    }

    @Test
    void canDeleteNotice() {
        //given
        Long noticeId = 1L;

        //when
        noticeService.deleteNotice(noticeId);

        //then
    }

}