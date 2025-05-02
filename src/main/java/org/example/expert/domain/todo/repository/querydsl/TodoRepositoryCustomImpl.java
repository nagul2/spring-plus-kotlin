package org.example.expert.domain.todo.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.todo.dto.response.TodoTitleResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.dto.TodoTitleSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

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

    @Override
    public Page<TodoTitleResponse> searchTodoTitles(TodoTitleSearchDto dto, Pageable pageable) {

        JPAQuery<TodoTitleResponse> contentQuery = queryFactory
                .select(Projections.constructor(TodoTitleResponse.class,
                        todo.id,
                        todo.title,
                        user.countDistinct(),
                        comment.countDistinct()))
                .from(todo)
                .leftJoin(todo.user, user)
                .leftJoin(todo.comments, comment)
                .where(titleContains(dto.getTitle()),
                        nicknameContains(dto.getNickname()),
                        createdAtBetween(dto.getCreateAtFrom(), dto.getCreateAtTo()))
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        JPAQuery<Long> countQuery = queryFactory
                .select(todo.countDistinct())
                .from(todo);

        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return nickname != null ? todo.user.nickname.contains(nickname) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null) return todo.createdAt.between(from, to);
        if (from != null) return todo.createdAt.goe(from);
        if (to != null) return todo.createdAt.loe(to);
        return null;
    }
}
