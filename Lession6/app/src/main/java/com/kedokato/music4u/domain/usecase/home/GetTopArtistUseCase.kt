package com.kedokato.music4u.domain.usecase.home

import com.kedokato.music4u.domain.repository.GetTopArtistsRepo

class GetTopArtistsUseCase(
    private val repo: GetTopArtistsRepo
) {
    suspend operator fun invoke() = repo.getTopArtists()
}