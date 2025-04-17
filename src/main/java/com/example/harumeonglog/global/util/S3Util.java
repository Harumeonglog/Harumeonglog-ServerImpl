package com.example.harumeonglog.global.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.harumeonglog.global.data.S3ConfigData;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class S3Util {
    private final AmazonS3 amazonS3Client;
    private final S3ConfigData s3ConfigData;


    //presigned url로 파일 업로드
    @Transactional
    public String uploadFile(MultipartFile file, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        //s3에 파일 업로드
        amazonS3Client.putObject(new PutObjectRequest(s3ConfigData.getBucket(), key, file.getInputStream(), metadata));

        return amazonS3Client.getUrl(s3ConfigData.getBucket(), key).toString();
    }

    // S3에서 파일 가져오기
    public String getFilePresignedUrl(String key, int expirationMinutes) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(s3ConfigData.getBucket(), key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(getExpirationDate(expirationMinutes));

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    // Presigned URL의 만료 시간 설정
    private Date getExpirationDate(int expirationMinutes) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * expirationMinutes; // 분 단위로 계산
        expiration.setTime(expTimeMillis);
        return expiration;
    }


    // S3 파일 삭제
    public void deleteFile(String key) {
        if (key == null && key.trim().isEmpty()) {
            throw new S3Exception(S3ErrorCode.NOT_FOUND);
        }
        try {
            amazonS3Client.deleteObject(s3ConfigData.getBucket(), key);
        } catch (AmazonS3Exception e) {
            throw new S3Exception(S3ErrorCode.DELETE_FAILED);
        }
    }
}
