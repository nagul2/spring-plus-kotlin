package org.example.expert.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.security.CustomUserDetails;
import org.example.expert.domain.image.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PutMapping("/profile")
    public ResponseEntity<String> updateUserProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam MultipartFile file) {

        String url = imageService.updateUserProfileImage(userDetails, file);
        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUserProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        imageService.deleteUserProfileImage(userDetails);
        return ResponseEntity.noContent().build();
    }
}
