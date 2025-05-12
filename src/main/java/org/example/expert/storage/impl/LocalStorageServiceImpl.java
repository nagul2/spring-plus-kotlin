package org.example.expert.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.storage.StorageService;
import org.example.expert.storage.dto.UploadResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@Profile("local")
@RequiredArgsConstructor
public class LocalStorageServiceImpl implements StorageService {

    @Value("${file.image-storage.path}")
    private String path;

    @Override
    public UploadResultDto upload(MultipartFile file, String dirName) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dirPath = Path.of(path, dirName);
        Path filePath = dirPath.resolve(filename);

        try {
            Files.createDirectories(dirPath);
            file.transferTo(filePath.toFile());
            log.info("로컬 파일 저장 성공: {}", filePath);
            return new UploadResultDto(
                    filePath.toAbsolutePath().toString(),
                    filename
                    );
        } catch (IOException e) {
            log.info("로컬 파일 저장 실패");
            throw new ServerException("로컬 파일 저장 실패");
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            Path path = Path.of(filePath);
            Files.deleteIfExists(path);
            log.info("로컬 파일 삭제 완료: {}", filePath);
        } catch (IOException e) {
            log.error("로컬 파일 삭제 실패", e);
            throw new ServerException("로컬 파일 삭제 실패");
        }
    }
}
