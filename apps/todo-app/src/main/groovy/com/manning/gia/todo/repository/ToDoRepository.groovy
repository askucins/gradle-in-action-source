package com.manning.gia.todo.repository

import com.manning.gia.todo.model.ToDoItem

interface ToDoRepository {

    List<ToDoItem> findAll()

    ToDoItem findById(Long id)

    Long insert(ToDoItem toDoItem)

    void update(ToDoItem toDoItem)

    void delete(ToDoItem toDoItem)
}
