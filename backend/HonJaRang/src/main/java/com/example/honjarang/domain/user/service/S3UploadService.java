package com.example.honjarang.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@RequiredArgsConstructor
@Service
public class S3UploadService {
    private final S3Client s3Client;

    public void delete(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket("honjarang")
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
