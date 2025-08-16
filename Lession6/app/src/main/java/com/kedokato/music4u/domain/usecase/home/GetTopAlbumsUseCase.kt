package com.kedokato.music4u.domain.usecase.home

import com.kedokato.music4u.domain.repository.GetAlbumsRepo

class GetTopAlbumsUseCase(
    private val repo: GetAlbumsRepo
) {
    suspend operator fun invoke() = repo.getTopAlbums()
}