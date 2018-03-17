package com.manning.gia.todo.model

import spock.lang.Specification
import spock.lang.Unroll

/*
An excerpt from "Spock: Up and Running"
by Rob Fletcher, O'Reilly Media, 2017

What Is == Really Doing?
Although it’s typical (and usually safe) to consider == an alias for the Java-style equals
method, that’s something of an oversimplification.
Even though we can think of
    assert a == b
as the Groovy equivalent to the Java
    assert a.equals(b);
in fact, if the class of a implements Comparable , Groovy will use:
    a.compareTo(b) == 0;
The primary reason this is done is so that the == operator can be reflexive between
java.lang.String and groovy.lang.GString (the class that backs Groovy’s interpo‐
lated strings). Because java.lang.String is final , Groovy cannot extend it. Subse‐
quent versions of Java have introduced the CharSequence class to work around this
kind of problem in alternative JVM languages, but Groovy’s implementation predates
this.
 */

class ToDoItemComparableSpec extends Specification {

    // TODO - check the == on sorted outcome as well!!

    @Unroll
    def "should compare TDI items (#tdi1, #tdi2)"() {
        expect:
        (tdi1 == tdi2) == outcome
        where:
        tdi1                              | tdi2                              || outcome
        new ToDoItem(id: 1)               | new ToDoItem(id: 1)               || true
        new ToDoItem(id: 1)               | new ToDoItem(id: 2)               || false
        new ToDoItem(id: 1, name: 'xyz1') | new ToDoItem(id: 1, name: 'xyz1') || true
        new ToDoItem(id: 1, name: 'xyz1') | new ToDoItem(id: 1, name: 'xyz2') || false
        new ToDoItem(name: 'xyz1')        | new ToDoItem(name: 'xyz1')        || true
        new ToDoItem(name: 'xyz1')        | new ToDoItem(name: 'xyz2')        || false

    }


    def "should default (field by field) order be used for TDI items on sort"() {
        when:
        def tdi1 = new ToDoItem(id: 1)
        def tdi2 = new ToDoItem(id: 2)
        then:
        [tdi1, tdi2].sort() == [tdi1, tdi2]
    }

    def "should <compareTo> be used for TDIC items on sort"() {
        when:
        def tdic1 = new ToDoItemComparable(id: 1)
        def tdic2 = new ToDoItemComparable(id: 2)
        then:
        [tdic1, tdic2].sort() == [tdic2, tdic1]
    }

    def "should sort use specific method for TDIC items"() {
        when:
        def tdic1 = new ToDoItemComparable(id: 0, name: 'Item 1')
        def tdic2 = new ToDoItemComparable(id: 0, name: 'Item 2')
        then:
        [tdic1, tdic2].sort({ a, b -> a.id <=> b.id ?: a.name <=> b.name ?: a.completed <=> b.completed }) == [tdic1, tdic2]
        and:
        [tdic1, tdic2].sort({ a, b -> a.compareTo(b) }) == [tdic2, tdic1]
    }
}