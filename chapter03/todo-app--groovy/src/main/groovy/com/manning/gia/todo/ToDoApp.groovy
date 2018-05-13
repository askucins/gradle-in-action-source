package com.manning.gia.todo

import com.manning.gia.todo.utils.CommandLineInput
import com.manning.gia.todo.utils.CommandLineInputHandler

class ToDoApp {
    static char DEFAULT_INPUT = '\u0000'

    static void main(String... args) {
        CommandLineInputHandler commandLineInputHandler = new CommandLineInputHandler()
        char command = DEFAULT_INPUT

        while (CommandLineInput.EXIT.getShortCmd() != command) {
            commandLineInputHandler.printOptions()
            String input = commandLineInputHandler.readInput()
            char[] inputChars = input.length() == 1 ? input.toCharArray() : [DEFAULT_INPUT] as char[]
            command = inputChars[0]
            CommandLineInput commandLineInput = CommandLineInput.getCommandLineInputForInput(command)
            commandLineInputHandler.processInput(commandLineInput)
        }
    }
}