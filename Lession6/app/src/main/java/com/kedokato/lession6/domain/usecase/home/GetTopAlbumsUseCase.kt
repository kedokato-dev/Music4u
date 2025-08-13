package com.kedokato.lession6.domain.usecase.home

import com.kedokato.lession6.domain.repository.GetAlbumsRepo

class GetTopAlbumsUseCase(
    private val repo: GetAlbumsRepo
) {
    suspend operator fun invoke() = repo.getTopAlbums()
}