package com.manning.gia.todo.model

import spock.lang.Specification
import spock.lang.Unroll

class ToDoItemComparableSpec extends Specification {

    def "should create new item"() {
        when:
        ToDoItemComparable tdic = new ToDoItemComparable(id: 1, name: 'test', completed: false)
        then:
        tdic.id == 1
        and:
        tdic.name == 'test'
        and:
        !tdic.completed
    }

    @Unroll
    def "should compare TDIC items (#tdic1, #tdic2)"() {
        expect:
        (new ToDoItemComparable(tdic1) == new ToDoItemComparable(tdic2)) == outcome
        where:
        tdic1                 | tdic2                 || outcome
        [id: 1]               | [id: 1]               || true
        [id: 1]               | [id: 2]               || false
        [id: 1, name: 'xyz1'] | [id: 1, name: 'xyz1'] || true
        [id: 1, name: 'xyz1'] | [id: 2, name: 'xyz1'] || false
        [id: 1, name: 'xyz1'] | [id: 1, name: 'xyz2'] || true // name is not used in compareTo !!

    }
}