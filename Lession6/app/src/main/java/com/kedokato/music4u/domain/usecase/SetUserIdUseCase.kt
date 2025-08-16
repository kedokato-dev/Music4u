package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.repository.UserRepo

class SetUserIdUseCase(
    private val userRepo: UserRepo
) {
    operator fun invoke(userId: Long) {
        userRepo.setUserId(userId)
    }
}