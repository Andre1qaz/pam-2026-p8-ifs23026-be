package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.TodoDAO
import org.delcom.dao.RefreshTokenDAO
import org.delcom.dao.UserDAO
import org.delcom.entities.Todo
import org.delcom.entities.RefreshToken
import org.delcom.entities.User
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun userDAOToModel(dao: UserDAO, baseUrl: String) = User(
    id = dao.id.value.toString(),
    name = dao.name,
    username = dao.username,
    password = dao.password,
    photo = dao.photo,
    urlPhoto = buildImageUrl(baseUrl, dao.photo ?: "/uploads/defaults/user.png"),
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)

fun refreshTokenDAOToModel(dao: RefreshTokenDAO) = RefreshToken(
    dao.id.value.toString(),
    dao.userId.toString(),
    dao.refreshToken,
    dao.authToken,
    dao.createdAt,
)

fun todoDAOToModel(dao: TodoDAO, baseUrl: String) = Todo(
    id = dao.id.value.toString(),
    userId = dao.userId.toString(),
    title = dao.title,
    description = dao.description,
    isDone =  dao.isDone,
    cover = dao.cover,
    urlCover = buildImageUrl(baseUrl, dao.cover ?: "/uploads/defaults/cover.png"),
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)

/**
 * Membangun URL publik gambar dari path relatif.
 * Contoh: "uploads/plants/uuid.png" → "http://host:port/static/plants/uuid.png"
 *
 * Folder "uploads/" pada path relatif dipetakan ke route "/static/"
 * yang dilayani oleh Ktor Static Content plugin.
 */
fun buildImageUrl(baseUrl: String, pathGambar: String): String {
    // Hilangkan prefix "uploads/" dan ganti dengan "/static/"

    val relativePath = pathGambar.removePrefix("uploads/")
    return "$baseUrl/static/$relativePath"
}