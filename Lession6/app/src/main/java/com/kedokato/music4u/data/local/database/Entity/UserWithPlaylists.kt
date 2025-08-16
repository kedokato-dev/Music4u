package com.kedokato.music4u.data.local.database.Entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPlaylists(
    @Embedded  val user: UserEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )

    val playlists: List<PlaylistEntity>
)