package com.kedokato.lession6.domain.usecase.home

import com.kedokato.lession6.domain.repository.GetTopArtistsRepo

class GetTopArtistsUseCase(
    private val repo: GetTopArtistsRepo
) {
    suspend operator fun invoke() = repo.getTopArtists()
}