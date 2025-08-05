package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.repository.UserRepo

class GetUserIdUseCase(
    private val userRepo: UserRepo
) {
    operator fun invoke(): Long? {
        return userRepo.getCurrentUserId()
    }
}