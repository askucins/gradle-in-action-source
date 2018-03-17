package com.manning.gia.todo.repository

import com.manning.gia.todo.model.ToDoItem
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/*
Since I don't have compareTo defined for ToDoItem I'm matching by 'id' explicitly
 */

@EqualsAndHashCode
@ToString(includeNames = true, includePackage = false)
class InMemoryToDoRepository implements ToDoRepository {

    private List<ToDoItem> repository = []

    @Override
    List<ToDoItem> findAll() {
        return repository
    }

    @Override
    ToDoItem findById(Long id) {
        return repository.find { it.id == id }
    }

    @Override
    Long insert(ToDoItem tdi) {
        repository.add(tdi)
        return tdi.id
    }

    @Override
    void update(ToDoItem tdi) {
        def tdiMatch = repository.find { it.id == tdi.id }
        tdiMatch.name = tdi.name
        tdi.completed = tdi.completed
    }

    @Override
    void delete(ToDoItem tdi) {
        repository.remove(tdi)
    }
}
