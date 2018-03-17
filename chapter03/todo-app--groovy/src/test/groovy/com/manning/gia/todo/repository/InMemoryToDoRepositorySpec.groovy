package com.manning.gia.todo.repository

import com.manning.gia.todo.model.ToDoItem
import spock.lang.Specification

class InMemoryToDoRepositorySpec extends Specification {

    def "should new created repository be empty"() {
        when:
        ToDoRepository repo = new InMemoryToDoRepository()
        then:
        repo.findAll().size() == 0
    }

    //TODO Questionable...
    def "should allow to delete an item from empty repository"() {
        given:
        ToDoItem tdi = new ToDoItem(id: 0, name: 'test')
        when:
        ToDoRepository repo = new InMemoryToDoRepository()
        then:
        repo.delete(tdi)
        and:
        repo.findAll().size() == 0
    }

    def "should insert an item to empty repository"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(id: 0, name: 'test')
        when:
        repo.insert(tdi)
        then:
        repo.findAll().contains(tdi)
        and:
        repo.findById(tdi.id) == tdi
    }

    def "should insert an item to a not empty repository"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test')
        repo.insert(tdi)
        ToDoItem tdiAnother = new ToDoItem(id: 2, name: 'test')
        when:
        repo.insert(tdiAnother)
        then:
        repo.findAll().sort({ it.id }) == [tdi, tdiAnother]
    }

    def "should insert 100 items to an empty repository"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        List<ToDoItem> items = []
        (0..<100).step(1, { items.add new ToDoItem(id: it, name: "test: $it") })
        when:
        items.each { item -> repo.insert(item) }
        then:
        repo.findAll().sort({ it.id }) == items
    }

    def "should remove an item from repository"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test')
        repo.insert(tdi) == tdi.id
        when:
        repo.delete(tdi)
        then:
        repo.findAll().size() == 0
        and:
        repo.findById(tdi.id) == null
    }

    //TODO Questionable...
    def "should remove only a requested item"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test 1')
        ToDoItem tdiSameId = new ToDoItem(id: 1, name: 'test 2')
        repo.insert(tdi) == tdi.id
        repo.insert(tdiSameId) == tdiSameId.id
        when:
        repo.delete(tdi)
        then:
        repo.findAll().size() == 1
        and:
        repo.findAll().first() == tdiSameId
    }

    //TODO Questionable...
    def "should remove only a requested item, even if there are more similar"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test 1')
        ToDoItem tdiSame = new ToDoItem(id: 1, name: 'test 1')
        repo.insert(tdi) == tdi.id
        repo.insert(tdiSame) == tdiSame.id
        when:
        repo.delete(tdi)
        then:
        repo.findAll().size() == 1
        and:
        repo.findAll().first() == tdiSame
    }

    def "should update all matching items if there are more similar"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test 1')
        ToDoItem tdiSame = new ToDoItem(id: 1, name: 'test 1')
        repo.insert(tdi) == tdi.id
        repo.insert(tdiSame) == tdiSame.id
        when:
        ToDoItem tdiUpdate = new ToDoItem(id: 1, name: 'update')
        repo.update(tdiUpdate)
        then:
        repo.findAll().grep { it.id == 1 }.every{ it.name == 'update' }
    }
}
