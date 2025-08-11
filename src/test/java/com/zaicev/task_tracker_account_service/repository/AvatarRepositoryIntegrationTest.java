package com.zaicev.task_tracker_account_service.repository;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.MinIOContainer;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AvatarRepositoryIntegrationTest {

    private final static String BUCKET_NAME = "avatars";

    private final static MinIOContainer minIOContainer = new MinIOContainer("minio/minio:RELEASE.2025-07-23T15-54-02Z");

    private static MinioClient minioClient;

    private static AvatarRepository avatarRepository;

    @BeforeAll
    static void setUp() throws Exception {
        minIOContainer.start();
        minioClient = MinioClient
                .builder()
                .endpoint(minIOContainer.getS3URL())
                .credentials(minIOContainer.getUserName(), minIOContainer.getPassword())
                .build();
        avatarRepository = new AvatarRepository(minioClient);
        avatarRepository.initBucket();
    }

    @AfterAll
    static void tearDown() {
        minIOContainer.stop();
    }

    @Test
    void saveAvatar_and_findAvatar_ShouldSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg",
                "hello-avatar".getBytes(StandardCharsets.UTF_8));

        avatarRepository.saveAvatar(1L, file);

        ByteArrayResource resource = avatarRepository.findAvatar(1L);
        assertEquals("hello-avatar", new String(resource.getByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    void isAvatarExist_WhenAvatarExist_ShouldReturnTrue() throws Exception {
        byte[] data = "test-data".getBytes();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object("avatar2.jpg")
                .stream(new ByteArrayInputStream(data), data.length, -1)
                .build());

        assertTrue(avatarRepository.isAvatarExist(2L));
    }

    @Test
    void isAvatarExist_WhenAvatarNotExists_ShouldReturnFalse() {
        assertFalse(avatarRepository.isAvatarExist(999L));
    }

    @Test
    void findAvatar_WhenAvatarNotExists_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> avatarRepository.findAvatar(888L));
    }

    @Test
    void deleteObject_ShouldSuccess() throws Exception {
        byte[] data = "to-delete".getBytes();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object("avatar3.jpg")
                .stream(new ByteArrayInputStream(data), data.length, -1)
                .build());

        avatarRepository.deleteObject(3L);
        assertFalse(avatarRepository.isAvatarExist(3L));
    }


}
