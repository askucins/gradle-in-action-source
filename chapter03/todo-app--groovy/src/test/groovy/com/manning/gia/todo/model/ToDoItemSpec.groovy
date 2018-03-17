package com.manning.gia.todo.model

import spock.lang.Specification
import spock.lang.Unroll

class ToDoItemSpec extends Specification {

    def "should create new item"() {
        when:
        ToDoItem tdi = new ToDoItem(id: 1, name: 'test', completed: false)
        then:
        tdi.id == 1
        and:
        tdi.name == 'test'
        and:
        !tdi.completed
    }

    @Unroll
    def "should compare TDI items (#tdi1, #tdi2)"() {
        expect:
        (new ToDoItem(tdi1) == new ToDoItem(tdi2)) == outcome
        where:
        tdi1                  | tdi2                  || outcome
        [id: 1]               | [id: 1]               || true
        [id: 1]               | [id: 2]               || false
        [id: 1]               | [id: 10]              || false
        [id: 1, name: 'xyz1'] | [id: 1, name: 'xyz1'] || true
        [id: 1, name: 'xyz1'] | [id: 1, name: 'xyz2'] || false
        [id: 1, name: 'xyz1'] | [id: 2, name: 'xyz1'] || false
        [name: 'xyz1']        | [name: 'xyz1']        || true
        [name: 'xyz1']        | [name: 'xyz2']        || false
    }

    def "should sort TDI items" () {
        when:
        ToDoItem tdi1 = new ToDoItem(id:0L, name:'test 0')
        ToDoItem tdi2 = new ToDoItem(id:10L, name:'test 10')
        List<ToDoItem> items = [tdi1, tdi2]
        then:
        items.sort().collect{it.id} == [tdi1.id, tdi2.id]
    }
}