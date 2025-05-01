package org.example.expert.domain.todo.repository.querydsl;

import org.example.expert.domain.todo.entity.Todo;

public interface TodoRepositoryCustom {

    Todo findByIdWithUser(Long todoId);

}
