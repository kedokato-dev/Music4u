package com.kedokato.music4u.domain.repository

interface UserPreferenceRepo {
    suspend fun saveUserId(userId: Long)
    suspend fun getUseId(): Long?
}