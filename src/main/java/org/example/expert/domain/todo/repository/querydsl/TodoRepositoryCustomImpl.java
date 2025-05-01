package org.example.expert.domain.todo.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.stereotype.Repository;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Todo findByIdWithUser(Long todoId) {
        return queryFactory.selectFrom(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();
    }
}
