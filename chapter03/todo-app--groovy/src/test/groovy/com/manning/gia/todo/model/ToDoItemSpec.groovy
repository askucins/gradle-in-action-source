package com.manning.gia.todo.model

import spock.lang.Specification

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

    def "should order items by id"() {
        given:
        ToDoItem tdi1 = new ToDoItem(id: 1)
        ToDoItem tdi2 = new ToDoItem(id: 2)
        def items = [tdi2, tdi1]
        when:
        items.sort()
        then:
        items == [tdi1, tdi2]
    }

    def "should order items by name if ids are equal"() {
        given:
        ToDoItem tdi1 = new ToDoItem(id: 0, name: 'Item 1')
        ToDoItem tdi2 = new ToDoItem(id: 0, name: 'Item 2')
        def items = [tdi2, tdi1]
        when:
        items.sort()
        then:
        items == [tdi1, tdi2]
    }
}