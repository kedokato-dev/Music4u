package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.repository.UserPreferenceRepo

class GetUserIdUseCaseShared  (private val userPreferenceRepository: UserPreferenceRepo
) {
    suspend operator fun invoke(): Long? {
        return userPreferenceRepository.getUseId()
    }
}