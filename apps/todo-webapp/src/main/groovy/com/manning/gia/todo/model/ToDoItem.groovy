package com.manning.gia.todo.model

import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString(includeNames = true, includePackage = false)
class ToDoItem implements Comparable<ToDoItem> {
    Long id
    String name
    boolean completed


    int compareTo(ToDoItem tdi) {
        // Please notice, that 'name' is not used here!
        // Which makes sense:
        // - there could be two different items with the same name
        // - there could be two identical items with the same name
        //   (which means that this is effectively an identity)
        return this.id.compareTo(tdi.id)
    }
}

/*
An excerpt from "Spock: Up and Running"
by Rob Fletcher, O'Reilly Media, 2017

What Is == Really Doing?
Although it’s typical (and usually safe) to consider == an alias for the Java-style
equals method, that’s something of an oversimplification.
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