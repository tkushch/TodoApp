package com.example.todoapp.data.repository

import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import java.time.LocalDateTime
import java.util.UUID

class TodoItemsRepository {
    private val todoItems: MutableList<TodoItem> = mutableListOf()

    init {
        fillDataHardCode()
    }

    fun getTodoItems(): List<TodoItem> {
        return todoItems
    }

    fun getUncompletedTodoItems(): List<TodoItem> {
        return todoItems.filter { !it.done }
    }

    fun addTodoItem(taskText: String, importance: Importance, deadline: LocalDateTime?) {
        val newTask = TodoItem(
            id = UUID.randomUUID().toString(),
            text = taskText,
            importance = importance,
            deadline = deadline,
            done = false,
            creationDate = LocalDateTime.now(),
            updatedDate = null
        )
        addTodoItem(newTask)
    }

    private fun addTodoItem(todoItem: TodoItem) {
        todoItems.add(todoItem)
    }

    fun updateTodoItem(
        taskId: String,
        text: String,
        importance: Importance,
        deadline: LocalDateTime?
    ) {
        val index = todoItems.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val oldItem = todoItems[index]
            val newItem = oldItem.copy(
                text = text,
                importance = importance,
                deadline = deadline,
                updatedDate = LocalDateTime.now()
            )
            todoItems[index] = newItem
        }
    }

    fun changeTodoItemDoneStatus(taskId: String) {
        val index = todoItems.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val oldItem = todoItems[index]
            val newItem = oldItem.copy(done = !oldItem.done)
            todoItems[index] = newItem
        }
    }


    fun findTodoItemById(id: String): TodoItem? {
        return todoItems.find { it.id == id }
    }

    fun removeTodoItemById(id: String): Boolean {
        return todoItems.removeIf { it.id == id }
    }


    private fun fillDataHardCode() {
        addTodoItem(
            TodoItem(
                "0",
                "Attend team meeting 2",
                Importance.MEDIUM,
                LocalDateTime.now().plusDays(4),
                true,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "1",
                "Buy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceries",
                Importance.LOW,
                null,
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "2",
                "Complete project report",
                Importance.HIGH,
                LocalDateTime.now().plusDays(1),
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "3",
                "Pay utility bills",
                Importance.MEDIUM,
                LocalDateTime.now().plusWeeks(1),
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "4",
                "Book doctor appointment",
                Importance.HIGH,
                LocalDateTime.now().plusDays(3),
                true,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(2)
            )
        )
        addTodoItem(
            TodoItem(
                "5",
                "Clean the house",
                Importance.LOW,
                null,
                true,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(1)
            )
        )
        addTodoItem(
            TodoItem(
                "6",
                "Attend team meeting",
                Importance.MEDIUM,
                LocalDateTime.now().plusDays(2),
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "7",
                "Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance",
                Importance.HIGH,
                LocalDateTime.now().plusMonths(1),
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "8",
                "Read a book",
                Importance.LOW,
                null,
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "9",
                "Exercise",
                Importance.MEDIUM,
                null,
                true,
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now().minusDays(1)
            )
        )
        addTodoItem(
            TodoItem(
                "10",
                "Plan vacation",
                Importance.LOW,
                LocalDateTime.now().plusMonths(3),
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "11",
                "Prepare for presentation",
                Importance.HIGH,
                LocalDateTime.now().plusDays(5),
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "12",
                "Organize workspace",
                Importance.MEDIUM,
                LocalDateTime.now().plusDays(10),
                false,
                LocalDateTime.now(),
                null
            )
        )
        addTodoItem(
            TodoItem(
                "13",
                "Buy groceries 2",
                Importance.HIGH,
                null,
                false,
                LocalDateTime.now(),
                null
            )
        )
    }

}