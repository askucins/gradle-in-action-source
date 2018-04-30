package com.manning.gia.todo.model

import spock.lang.Specification
import spock.lang.Unroll

class ToDoItemSpec extends Specification {

    @Unroll
    def "should create new item like (id:#id, name:#name, completed:#completed)"() {
        when:
        ToDoItem tdi = new ToDoItem(args)
        then:
        tdi.id == id
        and:
        tdi.name == name
        and:
        tdi.completed == completed

        where:
        args                                       || id   | name      | completed
        [:]                                        || null | null      | false
        [name: 'Test 01']                          || null | 'Test 01' | false
        [name: 'Test 02', completed: true]         || null | 'Test 02' | true
        [name: 'Test 03', completed: false]        || null | 'Test 03' | false
        [name: '', completed: false]               || null | ''        | false
        [name: 'Test 05', completed: false, id: 1] || 1    | 'Test 05' | false
    }

    @Unroll
    def "should compare items (#_tdi1, #_tdi2)"() {
        expect:
        (tdi1 <=> tdi2) == bySpaceShip
        and:
        (tdi1.compareTo(tdi2)) == byCTmethod
        and:
        (tdi1 == tdi2) == byOperator // relies on compareTo (class implements Comparable)
        and:
        (tdi1.equals(tdi2)) == byEquals

        where:
        _tdi1                 | _tdi2                 || bySpaceShip | byCTmethod | byOperator | byEquals
        [id: 1]               | [id: 1]               || 0           | 0          | true       | true
        [id: 1]               | [id: 2]               || -1          | -1         | false      | false
        [id: 1]               | [id: 10]              || -1          | -1         | false      | false
        [id: 1, name: 'xyz1'] | [id: 1, name: 'xyz1'] || 0           | 0          | true       | true
        [id: 1, name: 'xyz1'] | [id: 2, name: 'xyz1'] || -1          | -1         | false      | false
        [id: 1, name: 'xyz1'] | [id: 1, name: 'xyz2'] || 0           | 0          | true       | false // name is not used in compareTo !!

        tdi1 = new ToDoItem(_tdi1)
        tdi2 = new ToDoItem(_tdi2)
    }

    def "should sort items"() {
        when:
        ToDoItem tdi1 = new ToDoItem(id: 0L, name: 'test 0')
        ToDoItem tdi2 = new ToDoItem(id: 1L, name: 'test 1')
        ToDoItem tdi3 = new ToDoItem(id: 10L, name: 'test 10')
        List<ToDoItem> items = [tdi3, tdi2, tdi1]
        then:
        items.sort().collect { it.id } == [tdi1, tdi2, tdi3].collect { it.id }
    }
}