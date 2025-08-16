package com.kedokato.music4u.domain.usecase

import android.util.Log
import com.kedokato.music4u.domain.repository.UserRepo

class UpdateUserProfileUseCase(
    private val repo: UserRepo
) {
    suspend operator fun invoke(
        userId: Long,
        name: String,
        phone: String,
        university: String,
        description: String,
        avatarUrl: String?
    ): Int {
        Log.d("UpdateUserProfileUseCase", "Updating user $userId with avatarUrl: '$avatarUrl'")

        return repo.updateUserDetails(
            userId = userId,
            name = name,
            phone = phone,
            university = university,
            description = description,
            avatarUrl = avatarUrl
        )
    }
}