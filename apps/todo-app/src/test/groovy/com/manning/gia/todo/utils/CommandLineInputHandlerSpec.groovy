package com.manning.gia.todo.utils

import spock.lang.Specification
import static com.manning.gia.todo.utils.CommandLineInput.*

class CommandLineInputHandlerSpec extends Specification {

    CommandLineInputHandler cliHandler
    CommandLineInputHelper cliHelper

    def setup() {
        cliHelper = Mock(CommandLineInputHelper)
        cliHandler = new CommandLineInputHandler(helper: cliHelper)
    }

    def "should printOptions be delegated to the helper class"() {
        when:
        cliHandler.printOptions()
        then:
        1 * cliHelper.printOptions()
    }

    def "should readOptions be delegated to the helper class"() {
        when:
        cliHandler.readInput()
        then:
        1 * cliHelper.readInput()
    }

    def "should not 'exit' be delegated"() {
        when:
        cliHandler.processInput(EXIT)
        then:
        0 * cliHelper._
    }

    def "should processInput raise exception handler when called with a non-match argument"() {
        given: "CommandLineInput not matching to any real values"
        CommandLineInput CliNonMatch = GroovyMock(CommandLineInput)
        assert getCommandLineInputForInput(CliNonMatch.shortCmd) == null
        when: "processInput is called with a not-handled enum value"
        cliHandler.processInput(CliNonMatch)
        then: "exception is raised and handled "
        1 * cliHelper.handleUnknownInput()
    }

    def "should processInput raise exception handler when called with a null"() {
        when: "processInput is called with null"
        cliHandler.processInput(null)
        then: "exception is raised and handled "
        1 * cliHelper.handleUnknownInput()
    }

    def "should processInput request printing all items when called with FIND_ALL"() {
        when:
        cliHandler.processInput(FIND_ALL)
        then:
        1 * cliHelper.printAllToDoItems()
    }

    def "should processInput request printing a given item when called with FIND_BY_ID"() {
        when:
        cliHandler.processInput(FIND_BY_ID)
        then:
        1 * cliHelper.printToDoItem()
    }

    def "should processInput request inserting a given item when called with INSERT"() {
        when:
        cliHandler.processInput(INSERT)
        then:
        1 * cliHelper.insertToDoItem()
    }

    def "should processInput request updating a given item when called with UPDATE"() {
        when:
        cliHandler.processInput(UPDATE)
        then:
        1 * cliHelper.updateToDoItem()
    }

    def "should processInput request deleting a given item when called with DELETE"() {
        when:
        cliHandler.processInput(DELETE)
        then:
        1 * cliHelper.deleteToDoItem()
    }


}