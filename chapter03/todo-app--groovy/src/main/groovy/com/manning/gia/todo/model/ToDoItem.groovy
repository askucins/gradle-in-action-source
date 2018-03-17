package com.manning.gia.todo.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includeNames = true)
class ToDoItem {
    Long id
    String name
    boolean completed
}