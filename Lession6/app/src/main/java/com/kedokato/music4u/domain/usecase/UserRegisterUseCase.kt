package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.repository.UserRepo

class UserRegisterUseCase(
    private val repo: UserRepo
) {
    suspend operator fun invoke(username: String, password: String, email: String): Long {
        return repo.register(username, password, email)
    }
}