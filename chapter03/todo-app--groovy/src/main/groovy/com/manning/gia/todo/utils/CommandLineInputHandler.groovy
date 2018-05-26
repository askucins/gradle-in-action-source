package com.manning.gia.todo.utils

import static com.manning.gia.todo.utils.CommandLineInput.*

class CommandLineInputHandler {
    private CommandLineInputHelper helper = new CommandLineInputHelper()

    void printOptions() {
        helper.printOptions()
    }

    String readInput() {
        helper.readInput()
    }

    void processInput(CommandLineInput input) {
        if (input == null) {
            helper.handleUnknownInput()
        } else {
            switch (input) {
                case FIND_ALL:
                    helper.printAllToDoItems()
                    break
                case FIND_BY_ID:
                    helper.printToDoItem()
                    break
                case INSERT:
                    helper.insertToDoItem()
                    break
                case UPDATE:
                    helper.updateToDoItem()
                    break
                case DELETE:
                    helper.deleteToDoItem()
                    break
                case EXIT:
                    break
                default:
                    helper.handleUnknownInput()
            }
        }
    }
}