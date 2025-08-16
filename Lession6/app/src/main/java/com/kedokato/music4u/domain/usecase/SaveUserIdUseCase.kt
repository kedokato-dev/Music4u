package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.repository.UserPreferenceRepo

class SaveUserIdUseCase(
    private val userPreferenceRepository: UserPreferenceRepo
) {
    suspend operator fun invoke(userId: Long) {
        userPreferenceRepository.saveUserId(userId)
    }
}