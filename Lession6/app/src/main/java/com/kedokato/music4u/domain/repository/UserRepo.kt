package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.data.local.database.Entity.UserEntity

interface UserRepo {
    suspend fun login(username: String, password: String): UserEntity?

    suspend fun register(username: String, password: String, email: String): Long

    suspend fun getUserDetails(userId: Long): UserEntity

    suspend fun updateUserDetails(
        userId: Long,
        name: String,
        phone: String,
        university: String,
        description: String,
        avatarUrl: String?
    ): Int

    fun getCurrentUserId(): Long?

    fun setUserId(userId: Long)

    fun clearSession()

}