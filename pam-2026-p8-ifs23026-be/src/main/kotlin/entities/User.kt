package org.delcom.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    var id : String = UUID.randomUUID().toString(),
    var name: String,
    var username: String,
    var password: String,
    var photo: String? = null, // Path relatif file di server
    var urlPhoto: String = "", // URL publik yang dapat diakses langsung

    @Contextual
    val createdAt: Instant = Clock.System.now(),
    @Contextual
    var updatedAt: Instant = Clock.System.now(),
)