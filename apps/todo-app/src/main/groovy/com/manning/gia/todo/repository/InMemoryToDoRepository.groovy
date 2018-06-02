package com.manning.gia.todo.repository

import com.manning.gia.todo.model.ToDoItem
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong

/*
Assumptions:
- repository may keep only one item of a given id
- findAll return items sorted by id
- findById returns an item (on match) or null (no match)
- insert returns id (on success), or null (if there is already an item with that id in repository)
- delete doesn't do anything if repository doesn't have that item (not the id - the whole item!)
- update doesn't do anything if repository doesn't have an item of id like the argument
 */

@EqualsAndHashCode
@ToString(includeNames = true, includePackage = false)
class InMemoryToDoRepository implements ToDoRepository {
    AtomicLong currentId = new AtomicLong()
    ConcurrentMap<Long, ToDoItem> tdItems = new ConcurrentHashMap<Long, ToDoItem>()

    @Override
    List<ToDoItem> findAll() {
        tdItems.values().sort() as List<ToDoItem>
    }

    @Override
    ToDoItem findById(Long id) {
        tdItems[(id)]
    }

    @Override
    Long insert(ToDoItem tdi) {
        def id = currentId.incrementAndGet()
        tdi.id = id
        tdItems.putIfAbsent(id, tdi)
        return id
    }

    @Override
    void update(ToDoItem tdi) {
        tdItems.replace(tdi.id, tdi)
    }

    @Override
    void delete(ToDoItem tdi) {
        tdItems.remove(tdi.id)
    }
}
