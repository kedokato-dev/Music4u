package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.repository.UserRepo

class ClearUserIdUseCase(
    private val userRepo: UserRepo
) {
    operator fun invoke() {
        userRepo.clearSession()
    }
}