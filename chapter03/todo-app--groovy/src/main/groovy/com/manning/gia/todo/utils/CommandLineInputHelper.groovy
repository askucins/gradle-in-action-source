package com.manning.gia.todo.utils

import com.manning.gia.todo.model.ToDoItem
import com.manning.gia.todo.repository.InMemoryToDoRepository
import com.manning.gia.todo.repository.ToDoRepository

class CommandLineInputHelper {
    private ToDoRepository toDoRepository = new InMemoryToDoRepository()
    private scanner = new Scanner(System.in)

    void printOptions() {
        def usage = """
            --- To Do Application ---
            Please make a choice:
            (a)ll items
            (f)ind a specific item
            (i)nsert a new item
            (u)pdate an existing item
            (d)elete an existing item
            (e)xit"""
        println usage.stripIndent()
    }

    String readInput() {
        print "> "
        return scanner.nextLine()
    }

    void handleUnknownInput() {
        println "Please select a valid option!"
    }

    /* ************************************************************ */
    /* CREATE */

    void insertToDoItem() {
        ToDoItem toDoItem = askForNewToDoAction()
        Long id = toDoRepository.insert(toDoItem)
        println "Successfully inserted to do item with ID " + id + "."
    }

    private ToDoItem askForNewToDoAction() {
        ToDoItem toDoItem = new ToDoItem()
        println "Please enter the name of the item:"
        toDoItem.setName(readInput())
        return toDoItem
    }

    /* ************************************************************ */
    /* READ */

    void printToDoItem() {
        ToDoItem toDoItem = findToDoItem()

        if (toDoItem != null) {
            println(toDoItem)
        }
    }

    void printAllToDoItems() {
        Collection<ToDoItem> toDoItems = toDoRepository.findAll()

        if (toDoItems.isEmpty()) {
            println "Nothing to do. Go relax!"
        } else {
            for (ToDoItem toDoItem : toDoItems) {
                println toDoItem
            }
        }
    }

    private ToDoItem findToDoItem() {
        Long id = askForItemId()
        ToDoItem toDoItem = toDoRepository.findById(id)

        if (toDoItem == null) {
            System.err.println "To do item with ID " + id + " could not be found."
        }

        return toDoItem
    }

    private Long askForItemId() {
        println "Please enter the item ID:"
        String input = readInput()
        return Long.parseLong(input)
    }

    /* ************************************************************ */
    /* UPDATE */

    void updateToDoItem() {
        ToDoItem toDoItem = findToDoItem()

        if (toDoItem != null) {
            println toDoItem
            println "Please enter the name of the item:"
            toDoItem.setName(readInput())
            println "Please enter the done status the item:"
            toDoItem.setCompleted(Boolean.parseBoolean(readInput()))
            toDoRepository.update(toDoItem)
            println "Successfully updated to do item with ID " + toDoItem.getId() + "."
        }
    }

    /* ************************************************************ */
    /* DELETE */

    void deleteToDoItem() {
        ToDoItem toDoItem = findToDoItem()

        if (toDoItem != null) {
            toDoRepository.delete(toDoItem)
            println "Successfully deleted to do item with ID " + toDoItem.getId() + "."
        }
    }
}
