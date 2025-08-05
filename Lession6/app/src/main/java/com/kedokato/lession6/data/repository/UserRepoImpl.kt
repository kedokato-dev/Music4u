package com.kedokato.lession6.data.repository

import android.util.Log
import com.kedokato.lession6.data.local.database.DAO.UserDAO
import com.kedokato.lession6.data.local.database.Entity.UserEntity
import com.kedokato.lession6.data.session.SessionManager
import com.kedokato.lession6.domain.repository.UserRepo

class UserRepoImpl(
    private val userDao: UserDAO,
    private val sessionManager: SessionManager
): UserRepo {
    override suspend fun login(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): Long {
        return userDao.insertUser(UserEntity(username = username, password = password, email = email))
    }

    override suspend fun getUserDetails(userId: Long): UserEntity {
        return userDao.getUserById(userId)
    }

    override suspend fun updateUserDetails(
        userId: Long,
        name: String,
        phone: String,
        university: String,
        description: String,
        avatarUrl: String?
    ): Int {
        Log.d("UserRepoImpl", "Updating user details - avatarUrl: '$avatarUrl'")
            return userDao.updateUser(userId, name, phone, university, description, avatarUrl)
    }

    override fun getCurrentUserId(): Long? {
        return sessionManager.userId.value
    }

    override fun setUserId(userId: Long) {
        return sessionManager.setUserId(userId)
    }

    override fun clearSession() {
        return sessionManager.clear()
    }
}