package org.example.expert.domain.todo.repository.querydsl;

import org.example.expert.domain.todo.dto.response.TodoTitleResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.dto.TodoTitleSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {

    Todo findByIdWithUser(Long todoId);

    Page<TodoTitleResponse> searchTodoTitles(TodoTitleSearchDto searchDto, Pageable pageable);
}
