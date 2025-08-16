package com.kedokato.music4u.data.local.database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kedokato.music4u.data.local.database.converters.UriConverter

@Entity(tableName = "users")
@TypeConverters(UriConverter::class)
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val username: String,
    val password: String,
    @ColumnInfo(name = "avatarUri")
    val avatar: String? = null,
    @ColumnInfo(name = "fullName")
    val name: String? = null,
    val phone: String? = null,
    val email: String,
    val university: String?=null,
    val description: String?=null
)
