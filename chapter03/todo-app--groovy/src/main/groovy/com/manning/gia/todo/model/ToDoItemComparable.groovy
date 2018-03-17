package com.manning.gia.todo.model;

class ToDoItemComparable extends ToDoItem implements Comparable<ToDoItemComparable>{
    int compareTo(ToDoItemComparable tdic) {
        // For testing purposes. This is the inverse of <equals> order. See more in ToDoItemComparableSpec
        return (-1) * this.id.compareTo(tdic.id)
    }
}
