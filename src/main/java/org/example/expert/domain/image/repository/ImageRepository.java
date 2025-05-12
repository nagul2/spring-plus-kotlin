package org.example.expert.domain.image.repository;

import org.example.expert.domain.image.entity.Image;
import org.example.expert.domain.image.entity.ImageType;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUserAndType(User user, ImageType imageType);
}
