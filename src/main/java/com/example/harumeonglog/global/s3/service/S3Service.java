package com.example.harumeonglog.global.s3.service;

import com.example.harumeonglog.global.s3.dto.S3RequestDTO;
import com.example.harumeonglog.global.s3.enums.S3Domain;

import java.util.List;
import java.util.Map;

public interface S3Service {
    Map<String, String> generatePresignedUrl(String filename, String contentType, S3Domain domain, Long entityId);
    List<Map<String, String>> generatePresignedUrls(S3RequestDTO.GeneratePresignedUrlsRequest request);
}