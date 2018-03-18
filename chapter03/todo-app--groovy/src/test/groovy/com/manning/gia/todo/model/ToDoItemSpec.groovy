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