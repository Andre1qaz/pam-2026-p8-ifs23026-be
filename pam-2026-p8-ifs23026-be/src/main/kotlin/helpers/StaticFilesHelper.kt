package org.delcom.helpers

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

/**
 * Mendaftarkan folder "uploads/" sebagai direktori file statis
 * yang dapat diakses melalui URL "/static/...".
 *
 * Contoh akses:
 *   File di disk  : uploads/plants/uuid.png
 *   URL publik    : http://host:port/static/plants/uuid.png
 *   File di disk  : uploads/profile/me.png
 *   URL publik    : http://host:port/static/profile/me.png
 */
fun Application.configureStaticFiles() {
    val uploadDir = File("uploads")
    if (!uploadDir.exists()) {
        uploadDir.mkdirs()
    }

    routing {
        staticFiles("/static", uploadDir)
    }
}