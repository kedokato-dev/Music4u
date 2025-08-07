package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.repository.UserPreferenceRepo

class SaveUserIdUseCase(
    private val userPreferenceRepository: UserPreferenceRepo
) {
    suspend operator fun invoke(userId: Long) {
        userPreferenceRepository.saveUserId(userId)
    }
}