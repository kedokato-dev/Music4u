package com.kedokato.lession6.view.playlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kedokato.lession6.repoImpl.PlaylistRepoImpl
import com.kedokato.lession6.usecase.LoadSongsUseCase

class PlaylistViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = PlaylistRepoImpl( context.contentResolver)
        val useCase = LoadSongsUseCase(repo)

        @Suppress("UNCHECKED_CAST")
        return PlaylistViewModel(useCase) as T
    }
}
