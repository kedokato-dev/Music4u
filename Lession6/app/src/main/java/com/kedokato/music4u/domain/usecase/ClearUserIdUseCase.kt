package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.repository.UserRepo

class ClearUserIdUseCase(
    private val userRepo: UserRepo
) {
    operator fun invoke() {
        userRepo.clearSession()
    }
}