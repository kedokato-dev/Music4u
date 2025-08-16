package com.kedokato.music4u.domain.usecase.home

import com.kedokato.music4u.domain.repository.GetTopTracksRepo

class GetTopTracksUseCase(
    private val repo: GetTopTracksRepo
) {
    suspend operator fun invoke() = repo.getTopTracks()
}