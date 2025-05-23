package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.security.CustomUserDetails;
import org.example.expert.domain.common.dto.PageResponseDto;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.request.TodosSearchCond;
import org.example.expert.domain.todo.dto.request.TodosTitleSearchCond;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoTitleResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.todo.service.dto.TodoTitleSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<TodoSaveResponse> saveTodo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TodoSaveRequest todoSaveRequest
    ) {
        return ResponseEntity.ok(todoService.saveTodo(userDetails, todoSaveRequest));
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<TodoResponse>> getTodos(
            TodosSearchCond searchCond,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String weather = searchCond.getWeather();
        LocalDateTime fromDate = searchCond.getFrom();
        LocalDateTime toDate = searchCond.getTo();
        return ResponseEntity.ok(todoService.getTodos(page, size, weather, fromDate, toDate));
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable long todoId) {
        return ResponseEntity.ok(todoService.getTodo(todoId));
    }

    @GetMapping("/todos/titles")
    public ResponseEntity<PageResponseDto<TodoTitleResponse>> searchTodoTitles(
            TodosTitleSearchCond searchCond,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        TodoTitleSearchDto searchDto = TodoTitleSearchDto.from(searchCond);
        Page<TodoTitleResponse> pageTodoTitles = todoService.searchTodosTitles(searchDto, page, size);

        return ResponseEntity.ok(PageResponseDto.from(pageTodoTitles));
    }
}
