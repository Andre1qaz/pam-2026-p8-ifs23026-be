package org.delcom.repositories

import org.delcom.dao.TodoDAO
import org.delcom.entities.Todo
import org.delcom.helpers.suspendTransaction
import org.delcom.helpers.todoDAOToModel
import org.delcom.tables.TodoTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import java.util.*

class TodoRepository(private val baseUrl: String) : ITodoRepository {

    override suspend fun getAll(
        userId: String,
        search: String,
        page: Int,
        perPage: Int
    ): Pair<List<Todo>, Int> = suspendTransaction {
        val query = if (search.isBlank()) {
            TodoDAO.find {
                (TodoTable.userId eq UUID.fromString(userId))
            }.orderBy(TodoTable.createdAt to SortOrder.DESC)
        } else {
            val keyword = "%${search.lowercase()}%"
            TodoDAO.find {
                (TodoTable.userId eq UUID.fromString(userId)) and
                        (TodoTable.title.lowerCase() like keyword)
            }.orderBy(TodoTable.title to SortOrder.ASC)
        }

        val total = query.count().toInt()
        val offset = ((page - 1) * perPage).toLong()
        val items = query.limit(perPage, offset = offset)
            .map { todoDAOToModel(it, baseUrl) }

        Pair(items, total)
    }

    override suspend fun getById(todoId: String): Todo? = suspendTransaction {
        TodoDAO
            .find { (TodoTable.id eq UUID.fromString(todoId)) }
            .limit(1)
            .map { todoDAOToModel(it, baseUrl) }
            .firstOrNull()
    }

    override suspend fun create(todo: Todo): String = suspendTransaction {
        val todoDAO = TodoDAO.new {
            userId = UUID.fromString(todo.userId)
            title = todo.title
            description = todo.description
            cover = todo.cover
            isDone = todo.isDone
            createdAt = todo.createdAt
            updatedAt = todo.updatedAt
        }
        todoDAO.id.value.toString()
    }

    override suspend fun update(userId: String, todoId: String, newTodo: Todo): Boolean = suspendTransaction {
        val todoDAO = TodoDAO
            .find {
                (TodoTable.id eq UUID.fromString(todoId)) and
                        (TodoTable.userId eq UUID.fromString(userId))
            }
            .limit(1)
            .firstOrNull()

        if (todoDAO != null) {
            todoDAO.title = newTodo.title
            todoDAO.description = newTodo.description
            todoDAO.cover = newTodo.cover
            todoDAO.isDone = newTodo.isDone
            todoDAO.updatedAt = newTodo.updatedAt
            true
        } else {
            false
        }
    }

    override suspend fun delete(userId: String, todoId: String): Boolean = suspendTransaction {
        val rowsDeleted = TodoTable.deleteWhere {
            (TodoTable.id eq UUID.fromString(todoId)) and
                    (TodoTable.userId eq UUID.fromString(userId))
        }
        rowsDeleted >= 1
    }
}
