package com.manning.gia.todo.repository

import com.manning.gia.todo.model.ToDoItem
import spock.lang.Ignore
import spock.lang.Issue
import spock.lang.Specification
import spock.lang.Unroll

class InMemoryToDoRepositorySpec extends Specification {

    ToDoRepository repo

    def setup() {
        repo = new InMemoryToDoRepository()
    }

    def "should create an empty repository"() {
        expect:
        repo.findAll().size() == 0
    }

    def "should insert an item to an empty repository"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        when:
        Long insertedId = repo.insert(tdi)
        then: "item id is set while inserting"
        insertedId == tdi.id
    }

    def "should overwrite id of item while inserting to repository"() {
        given:
        Long id = -1
        ToDoItem tdi = new ToDoItem(id: id, name: 'test')
        when:
        Long newId = repo.insert(tdi)
        then: "id should be set on insert"
        tdi.id == newId
        and:
        newId != id
    }

    def "should overwrite id of item on repeated insert to repository"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        when:
        Long id = repo.insert(tdi)
        then: "id should be set on insert"
        tdi.id == id
        when:
        Long nextId = repo.insert(tdi)
        then: "id is overwritten"
        tdi.id == nextId
        and:
        id != nextId
    }

    def "should insert two items with the same names"() {
        given:
        String name = 'test'
        ToDoItem tdi = new ToDoItem(name: name)
        ToDoItem tdiNext = new ToDoItem(name: name)
        when:
        Long id = repo.insert(tdi)
        and:
        Long idNext = repo.insert(tdiNext)
        then:
        [tdi, tdiNext]*.id == [id, idNext]
        and:
        [tdi, tdiNext]*.name == [name, name]
    }

    def "should insert two items with different names"() {
        given:
        String name = 'test'
        String nameNext = 'test 2'
        ToDoItem tdi = new ToDoItem(name: name)
        ToDoItem tdiNext = new ToDoItem(name: nameNext)
        when:
        Long id = repo.insert(tdi)
        and:
        Long idNext = repo.insert(tdiNext)
        then:
        [tdi, tdiNext]*.id == [id, idNext]
        and: "names have not changed"
        [tdi, tdiNext]*.name == [name, nameNext]
    }

    def "should insert 100 items to an empty repository"() {
        given:
        List<ToDoItem> items = []
        (0..<100).step(1, { items.add new ToDoItem(name: "test: $it") })
        List<Long> insertedIds = []
        when:
        items.each { item -> insertedIds.add(repo.insert(item)) }
        then:
        items*.id == insertedIds
    }

    def "should findById return nothing if repository is empty"() {
        given:
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test')
        when:
        ToDoItem foundItem = repo.findById(tdi.id)
        then:
        foundItem == null
    }

    def "should findById return inserted item"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        Long id = repo.insert(tdi)
        assert tdi.id == id
        when:
        ToDoItem foundItem = repo.findById(tdi.id)
        then:
        foundItem == tdi
        // that's seldom too weak - hence more detailed tests needed:
        and:
        foundItem.id == tdi.id
        and:
        foundItem.name == tdi.name
    }

    @Unroll
    def "should findById find an item #item"() {
        given:
        List<ToDoItem> tdItems = [
                new ToDoItem(name: 'first'),
                new ToDoItem(name: 'middle'),
                new ToDoItem(name: 'last')
        ]
        tdItems.each { item -> repo.insert(item) }
        when:
        ToDoItem tdi = tdItems[(cid)]
        ToDoItem found = repo.findById(tdi.id)
        then:
        found.id == tdi.id
        and:
        found.name == tdi.name

        where:
        cid | item
        0   | 'inserted as the first'
        1   | 'inserted in the middle'
        2   | 'inserted as the last'
    }

    def "should findById find 100 inserted items"() {
        given:
        List<ToDoItem> items = []
        (0..<100).step(1, { items.add new ToDoItem(name: "test: $it") })
        items.each { item -> repo.insert(item) }
        when:
        List<ToDoItem> foundItems = []
        items.each { item -> foundItems.add(repo.findById(item.id)) }
        then:
        foundItems.collect { item -> [item.id, item.name] } == items.collect { item -> [item.id, item.name] }
    }

    def "should not findById return not inserted item"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        repo.insert(tdi)
        ToDoItem tdiNext = new ToDoItem(id: -1, name: 'not inserted')
        assert tdi.id != tdiNext.id
        when:
        ToDoItem found = repo.findById(tdiNext.id)
        then:
        found == null
    }

    def "should findAll return nothing if repository is empty"() {
        given:
        when:
        List<ToDoItem> foundItems = repo.findAll()
        then:
        foundItems.size() == 0
    }

    def "should findAll return inserted item"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        Long id = repo.insert(tdi)
        assert tdi.id == id
        when:
        List<ToDoItem> foundItems = repo.findAll()
        then:
        foundItems.collect { [it.id, it.name] } == [[tdi.id, tdi.name]]
    }

    def "should findAll return sorted items"() {
        given:
        List<ToDoItem> items = [
                new ToDoItem(name: 'first'),
                new ToDoItem(name: 'middle'),
                new ToDoItem(name: 'last')
        ]
        items.each { item -> repo.insert(item) }
        when:
        List<ToDoItem> foundItems = repo.findAll()
        then:
        foundItems.collect { [it.id, it.name] } == items.sort { it.id }.collect { [it.id, it.name] }
    }

    def "should findAll return 100 inserted items"() {
        given:
        List<ToDoItem> items = []
        (0..<100).step(1, { items.add new ToDoItem(name: "test: $it") })
        items.each { item -> repo.insert(item) }
        when:
        List<ToDoItem> foundItems = repo.findAll()
        then:
        foundItems.collect { [it.id, it.name] } == items.sort { it.id }.collect { [it.id, it.name] }
    }

    def "should not findByAll return not inserted item"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        repo.insert(tdi)
        ToDoItem tdiNext = new ToDoItem(id: -1, name: 'not inserted')
        assert tdi.id != tdiNext.id
        when:
        List<ToDoItem> foundItems = repo.findAll()
        then:
        foundItems.collect { [it.id, it.name] } == [[tdi.id, tdi.name]]
    }

    def "should update an item in the repository"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        Long inserted = repo.insert(tdi)
        assert tdi.id == inserted
        when:
        tdi.name = 'Updated'
        repo.update(tdi)
        ToDoItem tdiFound = repo.findById(tdi.id)
        then:
        tdiFound.id == tdi.id
        and:
        tdiFound.name == tdi.name
    }

    @Ignore
    @Issue("Update should be applied only after a call is made - there is a bug here!!")
    def "should update an item in the repository only after calling an update"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        Long inserted = repo.insert(tdi)
        assert tdi.id == inserted
        when:
        tdi.name = 'updated'
        then: "local changes are not propagated to the repository" //TODO This seems to be a bug!!
        repo.findById(tdi.id).name != tdi.name
        when:
        repo.update(tdi)
        then: "until repo is updated"
        repo.findById(tdi.id).name == tdi.name
    }

    def "should update do nothing if an item is not in repository"() {
        given:
        ToDoItem tdiBase = new ToDoItem(name: 'test')
        ToDoItem tdi = new ToDoItem(id: -1, name: 'update')
        repo.insert(tdiBase)
        assert tdiBase.id != tdi.id
        when: "tdi is not in repository"
        repo.update(tdi)
        then:
        def items = repo.findAll()
        items.size() == 1
        and:
        items.first().id == tdiBase.id
        and:
        items.first().name == tdiBase.name
    }

    def "should not delete item with a null id"() {
        given:
        ToDoItem tdi = new ToDoItem(id: null, name: 'test')
        when:
        repo.delete(tdi)
        then:
        NullPointerException e = thrown()
        and:
        repo.findAll().size() == 0
    }

    def "should delete do nothing if an item id is not in repository"() {
        given:
        ToDoItem tdiBase = new ToDoItem(name: 'Base')
        ToDoItem tdi = new ToDoItem(id: -1, name: 'Another')
        repo.insert(tdiBase)
        assert tdiBase.id != tdi.id
        when:
        repo.delete(tdi)
        then:
        def items = repo.findAll()
        items.size() == 1
        and:
        items.first().id == tdiBase.id
        and:
        items.first().name == tdiBase.name
    }

    def "should delete an item from repository"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        Long inserted = repo.insert(tdi)
        assert inserted == tdi.id
        when:
        repo.delete(tdi)
        then:
        repo.findAll().size() == 0
    }

    def "should delete an item from repository if there is an item with the same id in repo"() {
        given:
        ToDoItem tdiBase = new ToDoItem(name: 'test')
        repo.insert(tdiBase)
        when:
        ToDoItem tdi = new ToDoItem(id: tdiBase.id, name: 'different')
        repo.delete(tdi)
        then:
        repo.findAll().size() == 0
        and:
        repo.findById(tdiBase.id) == null
    }
}
