package com.zaicev.task_tracker_account_service.service;

import com.zaicev.task_tracker_account_service.repository.AvatarRepository;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequiredArgsConstructor
@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public void saveAvatar(Long userId, MultipartFile file) throws IOException, MinioException, GeneralSecurityException {
        if (avatarRepository.isAvatarExist(userId)){
            avatarRepository.deleteAvatar(userId);
        }
        avatarRepository.saveAvatar(userId, file);
    }

    public ByteArrayResource getAvatar(Long userId) throws IOException, MinioException, GeneralSecurityException{
        return avatarRepository.findAvatar(userId);
    }
}
