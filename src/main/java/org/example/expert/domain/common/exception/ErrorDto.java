package org.example.expert.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private final int statusCode;
    private final String message;
    private final LocalDateTime errorTime;
    private final String path;
}
