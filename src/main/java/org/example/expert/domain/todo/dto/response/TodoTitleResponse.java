package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodoTitleResponse {

    private final Long id;
    private final String title;
    private final Long managerCount;
    private final Long commentCount;

}
