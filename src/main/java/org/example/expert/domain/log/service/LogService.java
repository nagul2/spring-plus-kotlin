package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.log.dto.LogResponse;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Log successLogSave(Log log) {
        return logRepository.save(log);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Log failLogSave(Log log) {
        return logRepository.save(log);
    }

    public Page<LogResponse> findLogs(int page, int size) {
        if (page < 1) page = 1;
        if (size > 50 || size < 1) size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Log> findLogs = logRepository.findAll(pageable);
        return findLogs.map(LogResponse::from);
    }

}
