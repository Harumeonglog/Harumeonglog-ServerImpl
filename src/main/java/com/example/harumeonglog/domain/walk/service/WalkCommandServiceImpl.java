package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.repository.WalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkCommandServiceImpl implements WalkCommandService {

    private final WalkRepository walkRepository;

    @Override
    public WalkResponse.WalkStartResponse startWalk(WalkRequest.WalkStartRequest request) {

        return null;
    }

    @Override
    public WalkResponse.WalkPauseResponse pauseWalk(Long walkId) {
        return null;
    }

    @Override
    public WalkResponse.WalkResumeResponse resumeWalk(Long walkId) {
        return null;
    }

    @Override
    public WalkResponse.WalkEndResponse endWalk(Long walkId) {
        return null;
    }

    @Override
    public WalkResponse.WalkShareResponse shareWalk(Long id) {
        return null;
    }

    @Override
    public WalkResponse.WalkLikeResponse likeWalk(Long walkId) {
        return null;
    }
}
