package com.manning.gia.todo.utils

import com.manning.gia.todo.model.ToDoItem
import com.manning.gia.todo.repository.InMemoryToDoRepository
import com.manning.gia.todo.repository.ToDoRepository
import spock.lang.See
import spock.lang.Specification

@See(["https://stackoverflow.com/questions/47395946/spock-testing-output-from-printing-function",
        "https://stackoverflow.com/questions/3228427/redirect-system-out-println"])
class CommandLineInputHelperSpec extends Specification {

    Scanner scanner
    ByteArrayOutputStream buffer

    def setup() {
        scanner = GroovyMock(Scanner)
        buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        System.err = new PrintStream(buffer)
    }

    def cleanup() {
        System.out = new PrintStream(new FileOutputStream(FileDescriptor.out))
        System.err = new PrintStream(new FileOutputStream(FileDescriptor.err))
        println "[Test output:"
        println "${buffer.toString().trim()}"
        println "]"
        buffer.reset()
    }

    def "should readInput call that scanner"() {
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

    /* ************************************************************ */
    /* CREATE */

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
        when:
        ToDoItem tdi = toDoRepository.findAll().first()
        then:
        tdi.name == tdiName
        and:
        tdi.id > 0
    }

    /* ************************************************************ */
    /* READ */

    def "should print a requested ToDoItem"() {
        given:
        ToDoRepository toDoRepository = new InMemoryToDoRepository()
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        ToDoItem tdi = new ToDoItem(name: 'New TDI')
        toDoRepository.insert(tdi)
        and:
        scanner.nextLine() >> tdi.id
        when:
        helper.printToDoItem()
        then:
        buffer.toString().contains tdi.toString()
    }

    def "should handle a request to print non-existing ToDoItem"() {
        given:
        ToDoRepository toDoRepository = new InMemoryToDoRepository()
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        ToDoItem tdi = new ToDoItem(name: 'New TDI')
        toDoRepository.insert(tdi)
        Long nonExistingId = -tdi.id
        and:
        scanner.nextLine() >> nonExistingId
        when:
        helper.printToDoItem()
        then:
        buffer.toString().contains "To do item with ID $nonExistingId could not be found."
    }

    def "should print all items for non-empty repository"() {
        given:
        ToDoRepository toDoRepository = new InMemoryToDoRepository()
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        def tdis = [new ToDoItem(name: 'New TDI-1'), new ToDoItem(name: 'New TDI-2')]
        tdis.each { tdi -> toDoRepository.insert(tdi) }
        when:
        helper.printAllToDoItems()
        then:
        tdis.each { tdi -> assert buffer.toString().contains(tdi.toString()) }
    }

    def "should print all items for empty repository"() {
        given:
        ToDoRepository toDoRepository = new InMemoryToDoRepository()
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        when:
        helper.printAllToDoItems()
        then:
        buffer.toString().contains "Nothing to do. Go relax!"
    }

    /* ************************************************************ */
    /* UPDATE */

    def "should update an existing item"() {
        given:
        ToDoRepository toDoRepository = Spy(InMemoryToDoRepository)
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        ToDoItem tdi = new ToDoItem(name: 'New TDI')
        toDoRepository.insert(tdi)
        String newTdiName = 'Updated TDI'
        and:
        scanner.nextLine() >>> [tdi.id, newTdiName, true]
        when:
        helper.updateToDoItem()
        then:
        buffer.toString().contains "Successfully updated to do item with ID ${tdi.id}."
        and:
        1 * toDoRepository.update(tdi)
        and:
        with(toDoRepository.findById(tdi.id)) {
            assert name == newTdiName
            assert completed
        }

        cleanup:
        println "TDI updated: $tdi"
    }

    def "should handle an attempt to update a non-existing item"() {
        given:
        ToDoRepository toDoRepository = Spy(InMemoryToDoRepository)
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        ToDoItem tdi = new ToDoItem(name: 'New TDI')
        toDoRepository.insert(tdi)
        Long nonExistingId = -tdi.id
        and:
        scanner.nextLine() >> nonExistingId
        when:
        helper.updateToDoItem()
        then:
        buffer.toString().contains "To do item with ID $nonExistingId could not be found."
        and:
        0 * toDoRepository.update(_ as ToDoItem)
    }

    /* ************************************************************ */
    /* DELETE */

    def "should delete an existing item"() {
        given:
        ToDoRepository toDoRepository = Spy(InMemoryToDoRepository)
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        ToDoItem tdi = new ToDoItem(name: 'New TDI')
        toDoRepository.insert(tdi)
        and:
        scanner.nextLine() >> tdi.id
        when:
        helper.deleteToDoItem()
        then:
        buffer.toString().contains "Successfully deleted to do item with ID ${tdi.id}."
        and:
        1 * toDoRepository.delete(tdi)
    }

    def "should handle an attempt to delete a non-existing item"() {
        given:
        ToDoRepository toDoRepository = Spy(InMemoryToDoRepository)
        CommandLineInputHelper helper = new CommandLineInputHelper(scanner: scanner, toDoRepository: toDoRepository)
        and:
        ToDoItem tdi = new ToDoItem(name: 'New TDI')
        toDoRepository.insert(tdi)
        Long nonExistingId = -tdi.id
        and:
        scanner.nextLine() >> nonExistingId
        when:
        helper.deleteToDoItem()
        then:
        buffer.toString().contains "To do item with ID $nonExistingId could not be found."
        and:
        0 * toDoRepository.delete(_ as ToDoItem)
    }
}