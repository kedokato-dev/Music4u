package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.repository.UserRepo

class SetUserIdUseCase(
    private val userRepo: UserRepo
) {
    operator fun invoke(userId: Long) {
        userRepo.setUserId(userId)
    }
}