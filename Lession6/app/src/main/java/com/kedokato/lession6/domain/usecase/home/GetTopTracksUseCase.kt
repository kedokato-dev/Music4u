package com.kedokato.lession6.domain.usecase.home

import com.kedokato.lession6.domain.repository.GetTopTracksRepo

class GetTopTracksUseCase(
    private val repo: GetTopTracksRepo
) {
    suspend operator fun invoke() = repo.getTopTracks()
}