package com.manning.gia.todo.utils

import com.manning.gia.todo.model.ToDoItem
import com.manning.gia.todo.repository.InMemoryToDoRepository
import com.manning.gia.todo.repository.ToDoRepository
import spock.lang.Specification


class CommandLineInputHelperSpec extends Specification {

    Scanner scanner

    def setup() {
        scanner = GroovyMock(Scanner)
    }

    def "should readInput call that scanner :)"() {
        given:
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner)
        and:
        String consoleInput = 'New ToDoItem'
        scanner.nextLine() >> consoleInput
        when:
        helper.readInput()
        then:
        1 * scanner.nextLine()
    }

    def "should readInput get the input from console :)"() {
        given:
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner)
        and:
        String consoleInput = 'New ToDoItem'
        scanner.nextLine() >> consoleInput
        expect:
        helper.readInput() == consoleInput
    }

    def "should helper request inserting an ToDoItem"() {
        given:
        ToDoRepository toDoRepository = Mock(ToDoRepository)
        CommandLineInputHelper helper = new CommandLineInputHelper(toDoRepository: toDoRepository, scanner: scanner)
        and:
        String tdiName = 'New ToDoItem'
        scanner.nextLine() >> tdiName
        when:
        helper.insertToDoItem()
        then:
        // Only 'equals' can be used, because other comparators throw exception if 'id' is null
        // So this is a kind of workaround for
        // 1 * toDoRepository.insert(tdi)
        1 * toDoRepository.insert({ it.class == ToDoItem && it.equals(new ToDoItem(name: tdiName)) })
    }

    def "should insert a ToDoItem"() {
        given:
        ToDoRepository toDoRepository = new InMemoryToDoRepository()
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        String tdiName = 'New ToDoItem'
        scanner.nextLine() >> tdiName
        when:
        helper.insertToDoItem()
        then:
        toDoRepository.findAll().size() == 1
        and:
        toDoRepository.findAll().first().name == tdiName
    }
}