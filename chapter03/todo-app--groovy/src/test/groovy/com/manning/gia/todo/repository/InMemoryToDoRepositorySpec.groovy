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

    def "should delete any item from empty repository"() {
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
        Long insertedId = repo.insert(tdi)
        then:
        insertedId == tdi.id
    }

    def "should not insert an item if repository contains an item with the same id"() {
        given:
        ToDoItem tdiBase = new ToDoItem(id: 0, name: 'test')
        ToDoItem tdi = new ToDoItem(id: 0, name: 'test 2')
        assert tdiBase.id == tdi.id
        ToDoRepository repo = new InMemoryToDoRepository()
        repo.insert(tdiBase)
        when:
        def response = repo.insert(tdi)
        then: "no item inserted"
        response == null
        and:
        def items = repo.findAll()
        items.size() == 1
        and: "no existing item updated"
        items.first().equals(tdiBase) && !items.first().equals(tdi)
    }

    def "should insert an item to a not empty repository"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdiBase = new ToDoItem(id: 1, name: 'test')
        ToDoItem tdi = new ToDoItem(id: 2, name: 'test')
        repo.insert(tdiBase)
        when:
        Long inserted = repo.insert(tdi)
        then:
        inserted == tdi.id
        and:
        repo.findAll().sort({ it.id }) == [tdiBase, tdi]
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

    def "should delete an existing item from repository"() {
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

    def "should not delete an item if only id matches"() {
        given:
        ToDoItem tdiBase = new ToDoItem(id: 1, name: 'Base')
        ToDoItem tdi = new ToDoItem(id: 1, name: 'Another')
        assert tdiBase.id == tdi.id
        assert !tdiBase.equals(tdi)
        ToDoRepository repo = new InMemoryToDoRepository()
        repo.insert(tdiBase)
        when:
        repo.delete(tdi)
        then:
        def items = repo.findAll()
        items.size() == 1
        and:
        items.first().equals(tdiBase)
    }

    def "should delete do nothing if an item id is not in repository"() {
        given:
        ToDoItem tdiBase = new ToDoItem(id: 1, name: 'Base')
        ToDoItem tdi = new ToDoItem(id: 2, name: 'Another')
        assert tdiBase.id != tdi.id
        ToDoRepository repo = new InMemoryToDoRepository()
        repo.insert(tdiBase)
        when:
        repo.delete(tdi)
        then:
        def items = repo.findAll()
        items.size() == 1
        and:
        items.first().equals(tdiBase)
    }

    def "should update an item in the repository"() {
        given:
        ToDoItem tdiBase = new ToDoItem(id: 0, name: 'test')
        ToDoItem tdi = new ToDoItem(id: 0, name: 'update')
        assert tdiBase.id == tdi.id
        assert !tdiBase.equals(tdi)
        ToDoRepository repo = new InMemoryToDoRepository()
        repo.insert(tdiBase)
        when:
        repo.update(tdi)
        then: "item was updated, not added to repo"
        def items = repo.findAll().grep { it.id == tdi.id }
        items.size() == 1
        and:
        items.first().equals(tdi)
    }

    def "should update do nothing if an item is not in repository"() {
        given:
        ToDoItem tdiBase = new ToDoItem(id: 0, name: 'test')
        ToDoItem tdi = new ToDoItem(id: 1, name: 'update')
        assert tdiBase.id != tdi.id
        ToDoRepository repo = new InMemoryToDoRepository()
        repo.insert(tdiBase)
        when:
        repo.update(tdi)
        then:
        def items = repo.findAll()
        items.size() == 1
        and:
        items.first().equals(tdiBase)
    }

    def "should findById return nothing if an item with requested id is not in repository"() {
        given:
        ToDoItem tdiBase = new ToDoItem(id: 0, name: 'test')
        ToDoItem tdi = new ToDoItem(id: 1, name: 'update')
        assert tdiBase.id != tdi.id
        ToDoRepository repo = new InMemoryToDoRepository()
        repo.insert(tdiBase)
        when:
        def foundItem = repo.findById(tdi.id)
        then:
        foundItem == null
    }

    def "should findById return item if it is in repository"() {
        given:
        ToDoItem tdiBase = new ToDoItem(id: 0, name: 'base')
        ToDoItem tdi = new ToDoItem(id: 1, name: 'another')
        assert tdiBase.id != tdi.id
        ToDoRepository repo = new InMemoryToDoRepository()
        repo.insert(tdiBase)
        repo.insert(tdi)
        when:
        def foundItem = repo.findById(tdi.id)
        then:
        foundItem.equals(tdi)
    }

}
