package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.data.local.database.Entity.UserEntity
import com.kedokato.music4u.domain.repository.UserRepo

class UserAuthenticationUseCase(
    private val repo: UserRepo
) {
    suspend operator fun invoke(username: String, password: String) : UserEntity? {
        return repo.login(username, password)
    }
}