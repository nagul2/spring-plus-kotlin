package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodosSearchCond {
    private final String weather;
    private final LocalDateTime from;
    private final LocalDateTime to;
}
