package com.manning.gia.todo.utils

enum CommandLineInput {
    FIND_ALL('a' as char),
    FIND_BY_ID('f' as char),
    INSERT('i' as char),
    UPDATE('u' as char),
    DELETE('d' as char),
    EXIT('e' as char)

    char shortCmd

    private CommandLineInput(char shortCmd) {
        this.shortCmd = shortCmd
    }

    // To have a reverse search (char -> enum)
    static INPUTS = values().collectEntries { value -> [(value.shortCmd): value] }

    // A wrapper to that reverse search:
    static CommandLineInput getCommandLineInputForInput(char input) { INPUTS[(input)] }
}