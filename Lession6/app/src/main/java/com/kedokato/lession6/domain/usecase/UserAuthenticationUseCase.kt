package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.data.local.database.Entity.UserEntity
import com.kedokato.lession6.domain.repository.UserRepo

class UserAuthenticationUseCase(
    private val repo: UserRepo
) {
    suspend operator fun invoke(username: String, password: String) : UserEntity? {
        return repo.login(username, password)
    }
}