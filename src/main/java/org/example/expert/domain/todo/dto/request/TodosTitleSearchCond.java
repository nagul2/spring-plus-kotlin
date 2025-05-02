package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodosTitleSearchCond {
    private final String title;
    private final String nickname;
    private final LocalDateTime createAtFrom;
    private final LocalDateTime createAtTo;
}
