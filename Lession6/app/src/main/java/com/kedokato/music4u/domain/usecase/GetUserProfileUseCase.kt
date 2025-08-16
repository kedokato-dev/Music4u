package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.data.local.database.Entity.UserEntity
import com.kedokato.music4u.domain.repository.UserRepo

class GetUserProfileUseCase(
    private val repo : UserRepo
) {
    suspend operator fun  invoke (userId: Long): UserEntity  {
        return repo.getUserDetails(userId)
    }
}