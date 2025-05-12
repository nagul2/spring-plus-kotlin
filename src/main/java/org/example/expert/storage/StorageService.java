package org.example.expert.storage;

import org.example.expert.storage.dto.UploadResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    UploadResultDto upload(MultipartFile file, String dirName);

    void delete(String filePath);
}
