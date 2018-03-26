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
- findById returns an item (on match) or null (no match)
- insert returns id (on success), or null (if there is already an item with that id in repository)
- delete doesn't do anything if repository doesn't have that item (not the id - the whole item!)
- update doesn't do anything if repository doesn't have an item of id like the argument
 */

//TODO not completed

@EqualsAndHashCode
@ToString(includeNames = true, includePackage = false)
class InMemoryToDoRepository implements ToDoRepository {

    private AtomicLong currentId = new AtomicLong()
    private ConcurrentMap<Long, ToDoItem> tdItems = new ConcurrentHashMap<Long, ToDoItem>()

    @Override
    List<ToDoItem> findAll() {
       // List<ToDoItem> items = new ArrayList<ToDoItem>(items.values())
        List<ToDoItem> items = tdItems.values()
        return items.sort()
    }

    @Override
    ToDoItem findById(Long id) {
        return tdItems[(id)]
    }

    @Override
    Long insert(ToDoItem tdi) {
        if (tdItems.containsKey(tdi.id)) {
            return null
        } else {
            tdItems[(tdi.id)] = tdi
            return tdi.id
        }
    }

    @Override
    void update(ToDoItem tdi) {
        tdItems.replace(tdi.id, tdi)
    }

    @Override
    void delete(ToDoItem tdi) {
        tdItems.remove(tdi.id, tdi)
    }
}
