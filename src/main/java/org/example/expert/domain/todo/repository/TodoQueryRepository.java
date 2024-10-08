package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;

public interface TodoQueryRepository {
    TodoResponse findByIdWithUser(Long todoId);
}
