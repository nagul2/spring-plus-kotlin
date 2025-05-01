package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.querydsl.TodoRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN t.user " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);


    @Query("""
        select t from Todo t
        where (:weather is null or t.weather = :weather)
            and (:fromDate is null or t.modifiedAt >= :fromDate)
            and (:toDate is null or t.modifiedAt <= :toDate)
    """)
    Page<Todo> findAllTodos(Pageable pageable,
                            @Param("weather") String weather,
                            @Param("fromDate") LocalDateTime fromDate,
                            @Param("toDate") LocalDateTime toDate);
}
