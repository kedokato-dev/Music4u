package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.repository.UserPreferenceRepo

class GetUserIdUseCaseShared  (private val userPreferenceRepository: UserPreferenceRepo
) {
    suspend operator fun invoke(): Long? {
        return userPreferenceRepository.getUseId()
    }
}