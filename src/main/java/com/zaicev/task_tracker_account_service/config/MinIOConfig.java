package com.zaicev.task_tracker_account_service.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {
    @Value("${MINIO_SECRET_KEY}")
    private String secretToken;

    @Value("${MINIO_ACCESS_KEY}")
    private String accessToken;

    @Value("${MINIO_ENDPOINT}")
    private String minioEndpoint;

    @Value("${MINIO_PORT}")
    private int minioPort;

    @Bean
    MinioClient minioClient() {
        return MinioClient
                .builder()
                .endpoint(minioEndpoint, minioPort, false)
                .credentials(accessToken, secretToken)
                .build();
    }
}
