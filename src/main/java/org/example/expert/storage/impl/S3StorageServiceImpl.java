package org.example.expert.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.storage.StorageService;
import org.example.expert.storage.dto.UploadResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Profile("dev")
@RequiredArgsConstructor
public class S3StorageServiceImpl implements StorageService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String s3Url;


    @Override
    public UploadResultDto upload(MultipartFile file, String dirName) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String key = dirName + "/" + filename;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .acl("public-read")
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return new UploadResultDto(
                    s3Url + "/" + key,
                    filename
            );
        } catch (IOException | SdkClientException e) {
            log.error("s3 이미지 업로드 실패", e);
            throw new ServerException("S3 이미지 업로드 실패");
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            String key = filePath.replace(s3Url + "/", "");
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
        } catch (SdkClientException e) {
            log.error("s3 이미지 삭제 실패", e);
            throw new ServerException("이미지 삭제 실패");

        }
    }
}
