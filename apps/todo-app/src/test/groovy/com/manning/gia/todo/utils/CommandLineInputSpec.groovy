package com.manning.gia.todo.utils

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CommandLineInputSpec extends Specification {

    def "should getCommandLineInputForInput map correctly character input (#input) to proper action (#cml)"() {
        expect:
        CommandLineInput.getCommandLineInputForInput(input as char) == cml
        where:
        input || cml
        'a'   || CommandLineInput.FIND_ALL
        'f'   || CommandLineInput.FIND_BY_ID
        'i'   || CommandLineInput.INSERT
        'u'   || CommandLineInput.UPDATE
        'd'   || CommandLineInput.DELETE
        'e'   || CommandLineInput.EXIT
        'x'   || null
        'ab'  || CommandLineInput.FIND_ALL // Tricky Dicky
    }

    def "should short command (#shortCommand) be mapped correctly to action (#action)"() {
        expect:
        action.shortCmd == shortCommand as char
        where:
        shortCommand || action
        'a'          || CommandLineInput.FIND_ALL
        'f'          || CommandLineInput.FIND_BY_ID
        'i'          || CommandLineInput.INSERT
        'u'          || CommandLineInput.UPDATE
        'd'          || CommandLineInput.DELETE
        'e'          || CommandLineInput.EXIT
    }

}