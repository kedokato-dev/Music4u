package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.repository.UserRepo

class GetUserIdUseCase(
    private val userRepo: UserRepo
) {
    operator fun invoke(): Long? {
        return userRepo.getCurrentUserId()
    }
}