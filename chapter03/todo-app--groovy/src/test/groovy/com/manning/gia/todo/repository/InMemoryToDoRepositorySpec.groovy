package com.manning.gia.todo.repository

import com.manning.gia.todo.model.ToDoItem
import spock.lang.Specification
import spock.lang.Unroll

// TODO most of those tests are utterly wrong - those local values SHOULD NOT be modified!!
// FIXME!!!

class InMemoryToDoRepositorySpec extends Specification {

    def "should create an empty repository"() {
        when:
        ToDoRepository repo = new InMemoryToDoRepository()
        then:
        repo.findAll().size() == 0
    }

    def "should insert an item to an empty repository"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(name: 'test')
        when:
        Long insertedId = repo.insert(tdi)
        then: "item id is set while inserting"
        insertedId == tdi.id
    }

    def "should overwrite id of item on insert to repository"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
        List<ToDoItem> items = []
        (0..<100).step(1, { items.add new ToDoItem(name: "test: $it") })
        List<Long> inserted = []
        when:
        items.each { item -> inserted.add(repo.insert(item)) }
        then:
        items*.id == inserted
    }

    def "should findById return nothing if repository is empty"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test')
        when:
        ToDoItem foundItem = repo.findById(tdi.id)
        then:
        foundItem == null
    }

    def "should findById return inserted item"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
        List<ToDoItem> items = [
                new ToDoItem(name: 'first'),
                new ToDoItem(name: 'middle'),
                new ToDoItem(name: 'last')
        ]
        items.each { item -> repo.insert(item) }
        when:
        ToDoItem currentItem = items.get(cid)
        ToDoItem found = repo.findById(currentItem.id)
        then:
        found.id == currentItem.id
        and:
        found.name == currentItem.name

        where:
        cid | item
        0   | 'inserted as the first'
        1   | 'inserted in the middle'
        2   | 'inserted as the last'
    }

    def "should findById find 100 inserted items"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(name: 'test')
        repo.insert(tdi)
        ToDoItem tdiNext = new ToDoItem(id: 2, name: 'not inserted')
        assert tdi.id != tdiNext.id
        when:
        ToDoItem found = repo.findById(tdiNext.id)
        then:
        found == null
    }

    def "should findAll return nothing if repository is empty"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
        when:
        List<ToDoItem> foundItems = repo.findAll()
        then:
        foundItems.size() == 0
    }

    def "should findAll return inserted item"() {
        given:
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
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
        ToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(name: 'test')
        repo.insert(tdi)
        ToDoItem tdiNext = new ToDoItem(id: 2, name: 'not inserted')
        assert tdi.id != tdiNext.id
        when:
        List<ToDoItem> foundItems = repo.findAll()
        then:
        foundItems.collect { [it.id, it.name] } == [[tdi.id, tdi.name]]
    }

    def "should update an item in the repository" () {
        given:
        InMemoryToDoRepository repo = new InMemoryToDoRepository()
        ToDoItem tdi = new ToDoItem(name: 'test', completed: false)
        Long id = repo.insert(tdi)
        assert tdi.id == id
        when:
        tdi.name = 'updated'
        then: "local changes are not propagated to the repository"
        repo.findById(id).name != tdi.name
        when:
        repo.update(tdi)
        then:"until repo is updated"
        repo.findById(id).name == tdi.name
    }


    /*

    def "should not delete item without id"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        ToDoRepository repo = new InMemoryToDoRepository()
        when:
        repo.delete(tdi)
        then:
        IllegalArgumentException e = thrown()
        and:
        repo.findAll().size() == 0
    }

    def "should delete an item from repository"() {
        given:
        ToDoItem tdi = new ToDoItem(name: 'test')
        ToDoRepository repo = new InMemoryToDoRepository()
        Long insertedId = repo.insert(tdi)
        assert insertedId == tdi.id
        when:
        repo.delete(tdi)
        then:
        repo.findAll().size() == 0
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

    def "should findAll return sorted collection of items from repository" () {
        expect:
        false
    }
*/
}
