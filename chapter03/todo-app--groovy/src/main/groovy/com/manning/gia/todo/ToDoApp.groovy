package com.manning.gia.todo

import com.manning.gia.todo.utils.CommandLineInput
import com.manning.gia.todo.utils.CommandLineInputHandler

class ToDoApp {
    static char DEFAULT_INPUT = '\u0000'

    static void main(String... args) {
        CommandLineInputHandler commandLineInputHandler = new CommandLineInputHandler()
        char command = DEFAULT_INPUT

        while (CommandLineInput.EXIT.shortCmd != command) {
            commandLineInputHandler.printOptions()
            String input = commandLineInputHandler.readInput()
            command = input.size() == 1 ? input.getChars()[0] : DEFAULT_INPUT
            commandLineInputHandler.processInput(CommandLineInput.getCommandLineInputForInput(command))
        }
    }
}