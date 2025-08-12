package com.zaicev.task_tracker_account_service.repository;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AvatarRepository {

    @Getter
    private static final String AVATAR_BUCKET_NAME = "avatars";

    private static final String ACCOUNT_AVATAR_PATH_TEMPLATE = "avatar%d.jpg";

    private final MinioClient minioClient;

    @PostConstruct
    public void initBucket() throws Exception{
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(AVATAR_BUCKET_NAME).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(AVATAR_BUCKET_NAME).build());
        }
    }

    public void saveAvatar(Long userId, MultipartFile file) throws IOException, MinioException, GeneralSecurityException {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(AVATAR_BUCKET_NAME)
                    .object(ACCOUNT_AVATAR_PATH_TEMPLATE.formatted(userId))
                    .stream(inputStream, file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public boolean isAvatarExist(Long userId) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(AVATAR_BUCKET_NAME)
                    .object(ACCOUNT_AVATAR_PATH_TEMPLATE.formatted(userId))
                    .build());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public ByteArrayResource findAvatar(Long userId) throws IOException, MinioException, GeneralSecurityException, EntityNotFoundException {
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(AVATAR_BUCKET_NAME)
                .object(ACCOUNT_AVATAR_PATH_TEMPLATE.formatted(userId))
                .build())) {
            return new ByteArrayResource(stream.readAllBytes());
        } catch (ErrorResponseException errorResponseException) {
            if (errorResponseException.errorResponse().code().equals("NoSuchKey")) {
                throw new EntityNotFoundException("%s not found".formatted(ACCOUNT_AVATAR_PATH_TEMPLATE.formatted(userId)));
            }
            throw errorResponseException;
        }
    }

    public void deleteAvatar(Long userId) throws IOException, MinioException, GeneralSecurityException {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(AVATAR_BUCKET_NAME)
                .object(ACCOUNT_AVATAR_PATH_TEMPLATE.formatted(userId))
                .build());
    }
}
