package org.example.expert.domain.log.controller;


import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.PageResponseDto;
import org.example.expert.domain.log.dto.LogResponse;
import org.example.expert.domain.log.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping
    public ResponseEntity<PageResponseDto<LogResponse>> findLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<LogResponse> logs = logService.findLogs(page, size);
        PageResponseDto<LogResponse> from = PageResponseDto.from(logs);
        return ResponseEntity.ok(from);
    }
}
