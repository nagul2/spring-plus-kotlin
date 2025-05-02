package org.example.expert.domain.todo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.todo.dto.request.TodosTitleSearchCond;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodoTitleSearchDto {
    private final String title;
    private final String nickname;
    private final LocalDateTime createAtFrom;
    private final LocalDateTime createAtTo;

    public static TodoTitleSearchDto from(TodosTitleSearchCond searchCond) {
        return new TodoTitleSearchDto(
                searchCond.getTitle(),
                searchCond.getNickname(),
                searchCond.getCreateAtFrom(),
                searchCond.getCreateAtTo()
        );
    }
}
