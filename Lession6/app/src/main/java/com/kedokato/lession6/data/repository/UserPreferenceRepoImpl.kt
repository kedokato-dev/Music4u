package com.kedokato.lession6.data.repository

import com.kedokato.lession6.data.local.shared_prefer.UserPreferenceDataSource
import com.kedokato.lession6.domain.repository.UserPreferenceRepo

class UserPreferenceRepoImpl(
    private val userPreferenceDataSource: UserPreferenceDataSource
) : UserPreferenceRepo {

    override suspend fun saveUserId(userId: Long) {
        val userId = userId ?: 0L
        userPreferenceDataSource.saveUserId(userId)
    }

    override suspend fun getUseId(): Long? {
        val userId = userPreferenceDataSource.getUserId()
        return userId
    }
}