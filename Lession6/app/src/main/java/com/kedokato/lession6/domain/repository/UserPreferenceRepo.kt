package com.kedokato.lession6.domain.repository

interface UserPreferenceRepo {
    suspend fun saveUserId(userId: Long)
    suspend fun getUseId(): Long?
}