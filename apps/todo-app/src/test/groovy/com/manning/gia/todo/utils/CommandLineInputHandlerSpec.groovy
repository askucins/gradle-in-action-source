package com.manning.gia.todo.utils

import spock.lang.Specification
import static com.manning.gia.todo.utils.CommandLineInput.*

class CommandLineInputHandlerSpec extends Specification {

    CommandLineInputHandler clih
    CommandLineInputHelper helper

    def setup() {
        helper = Mock(CommandLineInputHelper)
        clih = new CommandLineInputHandler(helper: helper)
    }

    def "should printOptions be delegated to the helper class"() {
        when:
        clih.printOptions()
        then:
        1 * helper.printOptions()
    }

    def "should readOptions be delegated to the helper class"() {
        when:
        clih.readInput()
        then:
        1 * helper.readInput()
    }

    def "should not 'exit' be delegated"() {
        when:
        clih.processInput(EXIT)
        then:
        0 * helper._
    }

    def "should processInput raise exception handler when called with a non-match argument"() {
        given: "CommandLineInput not matching to any real values"
        CommandLineInput CliNonMatch = GroovyMock(CommandLineInput)
        assert getCommandLineInputForInput(CliNonMatch.shortCmd) == null
        when: "processInput is called with a not-handled enum value"
        clih.processInput(CliNonMatch)
        then: "exception is raised and handled "
        1 * helper.handleUnknownInput()
    }

    def "should processInput raise exception handler when called with a null"() {
        when: "processInput is called with null"
        clih.processInput(null)
        then: "exception is raised and handled "
        1 * helper.handleUnknownInput()
    }

    def "should processInput request printing all items when called with FIND_ALL"() {
        when:
        clih.processInput(FIND_ALL)
        then:
        1 * helper.printAllToDoItems()
    }

    def "should processInput request printing a given item when called with FIND_BY_ID"() {
        when:
        clih.processInput(FIND_BY_ID)
        then:
        1 * helper.printToDoItem()
    }

    def "should processInput request inserting a given item when called with INSERT"() {
        when:
        clih.processInput(INSERT)
        then:
        1 * helper.insertToDoItem()
    }

    def "should processInput request updating a given item when called with UPDATE"() {
        when:
        clih.processInput(UPDATE)
        then:
        1 * helper.updateToDoItem()
    }

    def "should processInput request deleting a given item when called with DELETE"() {
        when:
        clih.processInput(DELETE)
        then:
        1 * helper.deleteToDoItem()
    }


}