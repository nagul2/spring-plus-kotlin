package org.example.expert.domain.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.log.entity.DomainType;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.entity.LogStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LogResponse {

    private final Long id;
    private final String methodName;
    private final DomainType type;
    private final LogStatus status;
    private LocalDateTime createBy;

    public static LogResponse from(Log log) {
        return new LogResponse(
                log.getId(),
                log.getMethodName(),
                log.getType(),
                log.getStatus(),
                log.getCreateBy()
        );
    }
}
