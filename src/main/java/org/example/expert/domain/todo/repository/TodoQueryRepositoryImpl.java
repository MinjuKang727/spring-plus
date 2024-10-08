package org.example.expert.domain.todo.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.stereotype.Repository;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepositoryImpl implements TodoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public TodoResponse findByIdWithUser(Long todoId) {
        Tuple tuple = jpaQueryFactory
                .select(todo,
                        user.id,
                        user.email,
                        user.nickname
                )
                .from(todo)
                .join(todo.user, user)
                .where(todoIdEq(todoId))
                .fetchFirst();

        return new TodoResponse(
                tuple.get(todo).getId(),
                tuple.get(todo).getTitle(),
                tuple.get(todo).getContents(),
                tuple.get(todo).getWeather(),
                new UserResponse(tuple.get(todo).getUser().getId(), tuple.get(user.email), tuple.get(user.nickname)),
                tuple.get(todo).getCreatedAt(),
                tuple.get(todo).getModifiedAt()
        );
    }

    private BooleanExpression todoIdEq(Long todoId) {
        return todoId != null ? todo.id.eq(todoId) : Expressions.TRUE;
    }
}
