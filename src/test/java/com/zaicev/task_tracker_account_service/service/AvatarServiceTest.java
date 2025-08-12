package com.zaicev.task_tracker_account_service.service;

import com.zaicev.task_tracker_account_service.repository.AvatarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTest {

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private AvatarService avatarService;

    private final long userId = 1L;
    private static MultipartFile testFile;

    @BeforeAll
    static void setUp() {
        testFile = new MockMultipartFile(
                "avatar",
                "avatar.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake image content".getBytes()
        );
    }

    @Test
    void saveAvatar_whenAvatarNotExist_shouldSaveDirectly() throws Exception {
        Mockito.when(avatarRepository.isAvatarExist(userId)).thenReturn(false);

        avatarService.saveAvatar(userId, testFile);

        Mockito.verify(avatarRepository, Mockito.never()).deleteAvatar(userId);
        Mockito.verify(avatarRepository).saveAvatar(userId, testFile);
    }

    @Test
    void saveAvatar_whenAvatarExist_shouldDeleteAndSave() throws Exception {
        Mockito.when(avatarRepository.isAvatarExist(userId)).thenReturn(true);

        avatarService.saveAvatar(userId, testFile);

        Mockito.verify(avatarRepository).deleteAvatar(userId);
        Mockito.verify(avatarRepository).saveAvatar(userId, testFile);
    }

    @Test
    void getAvatar_shouldReturnResourceFromRepository() throws Exception {
        ByteArrayResource resource = new ByteArrayResource("fake image content".getBytes());
        Mockito.when(avatarRepository.findAvatar(userId)).thenReturn(resource);

        ByteArrayResource result = avatarService.getAvatar(userId);

        Assertions.assertArrayEquals(resource.getByteArray(), result.getByteArray());
        Mockito.verify(avatarRepository).findAvatar(userId);
    }
}
