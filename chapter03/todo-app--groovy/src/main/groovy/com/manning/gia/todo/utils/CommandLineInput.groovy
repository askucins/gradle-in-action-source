package com.manning.gia.todo.utils


enum CommandLineInput {
    FIND_ALL('a' as char), FIND_BY_ID('f' as char), INSERT('i' as char), UPDATE('u' as char), DELETE('d' as char), EXIT('e' as char)

    private final static Map<Character, CommandLineInput> INPUTS

    static {
        INPUTS = new HashMap<Character, CommandLineInput>()

        for (CommandLineInput input : values()) {
            INPUTS.put(input.getShortCmd(), input)
        }
    }

    private final char shortCmd

    private CommandLineInput(char shortCmd) {
        this.shortCmd = shortCmd
    }

    char getShortCmd() {
        return shortCmd
    }

    static CommandLineInput getCommandLineInputForInput(char input) {
        return INPUTS.get(input)
    }
}