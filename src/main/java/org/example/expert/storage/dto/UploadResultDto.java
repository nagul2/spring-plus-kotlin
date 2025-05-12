package org.example.expert.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadResultDto {

    private final String imageUrl;
    private final String filename;
}
