package com.example.harumeonglog.global.s3.controller;

import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.s3.dto.S3RequestDTO;
import com.example.harumeonglog.global.s3.enums.S3Domain;
import com.example.harumeonglog.global.s3.service.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
@Tag(name = "S3", description = "S3 이미지 업로드 관련 API")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned-url")
    public CustomResponse<Map<String, String>> getPresignedUrl(
            @RequestParam String filename,
            @RequestParam String contentType,
            @RequestParam S3Domain domain,
            @RequestParam(required = false) Long entityId) {
        return CustomResponse.ok(s3Service.generatePresignedUrl(filename, contentType, domain, entityId));
    }

    @PostMapping("/presigned-urls")
    public CustomResponse<List<Map<String, String>>> getPresignedUrls(
            @RequestBody S3RequestDTO.GeneratePresignedUrlsRequest request) {
        return CustomResponse.ok(s3Service.generatePresignedUrls(request));
    }
}