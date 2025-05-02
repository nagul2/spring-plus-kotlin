package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String methodName;

    @Enumerated(EnumType.STRING)
    private DomainType type;

    @Enumerated(EnumType.STRING)
    private LogStatus status;

    private LocalDateTime createBy;

    public Log(String methodName, DomainType type, LogStatus status) {
        this.methodName = methodName;
        this.type = type;
        this.status = status;
        this.createBy = LocalDateTime.now();
    }
}
