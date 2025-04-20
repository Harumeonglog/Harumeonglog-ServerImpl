package com.example.harumeonglog.global.s3.dto;

import com.example.harumeonglog.global.s3.enums.S3Domain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class S3RequestDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneratePresignedUrlsRequest {
        private List<String> filenames;
        private List<String> contentTypes;
        private S3Domain domain;
        private Long entityId;
    }
}