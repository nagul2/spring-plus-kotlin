package org.example.expert.domain.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.security.CustomUserDetails;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.image.entity.Image;
import org.example.expert.domain.image.entity.ImageType;
import org.example.expert.domain.image.repository.ImageRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.storage.StorageService;
import org.example.expert.storage.dto.UploadResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    public static final String PROFILE_DIR_NAME = "profile";
    public static final String DEFAULT = "default";

    private final StorageService storageService;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${default-image.profile}")
    private String defaultProfile;

    @Transactional
    public String saveUserProfileImage(User user, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            imageRepository.save(Image.createProfile(user, DEFAULT, DEFAULT, defaultProfile, DEFAULT, 0));
            return defaultProfile;
        }
        UploadResultDto uploadResult = storageService.upload(file, PROFILE_DIR_NAME);

        String originFilename = file.getOriginalFilename();
        String extension = extractExtension(originFilename);
        Image image = Image.createProfile(user, originFilename, uploadResult.getFilename(), uploadResult.getImageUrl(), extension, file.getSize());

        imageRepository.save(image);
        return uploadResult.getImageUrl();
    }

    @Transactional
    public String updateUserProfileImage(CustomUserDetails userDetails, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("업로드할 프로필 이미지가 없습니다.");
        }
        deleteUserProfileImage(userDetails);

        UploadResultDto uploadResult = storageService.upload(file, PROFILE_DIR_NAME);
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new InvalidRequestException("User not found"));

        String originFilename = file.getOriginalFilename();
        String extension = extractExtension(originFilename);
        Image image = Image.createProfile(user, originFilename, uploadResult.getFilename(), uploadResult.getImageUrl(), extension, file.getSize());

        imageRepository.save(image);
        return uploadResult.getImageUrl();
    }

    @Transactional
    public void deleteUserProfileImage(CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new InvalidRequestException("User not found"));

        Image image = imageRepository.findByUserAndType(user, ImageType.PROFILE)
                .orElseThrow(() -> new InvalidRequestException("Profile Image not found"));

        storageService.delete(image.getImageUrl());
        imageRepository.delete(image);
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new InvalidRequestException("Invalid file name");
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
    }
}
