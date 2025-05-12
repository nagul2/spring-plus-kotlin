package org.example.expert.domain.image.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.entity.User;

import static kotlin.reflect.jvm.internal.impl.metadata.deserialization.ProtoTypeTableUtilKt.type;

@Getter
@Entity
@NoArgsConstructor
public class Image extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String originFilename;
    private String filename;
    private String imageUrl;
    private String extension;
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    private ImageType type;

    public Image(User user, String originFilename, String filename, String imageUrl, String extension, Long fileSize, ImageType type) {
        this.user = user;
        this.originFilename = originFilename;
        this.filename = filename;
        this.imageUrl = imageUrl;
        this.extension = extension;
        this.fileSize = fileSize;
        this.type = type;
    }

    public static Image createProfile(User user, String originFilename, String filename, String imageUrl, String extension, long fileSize) {
        return new Image(user, originFilename, filename, imageUrl, extension, fileSize, ImageType.PROFILE);
    }
}
