package com.kedokato.lession6.presentation.home

import androidx.lifecycle.ViewModel
import com.kedokato.lession6.domain.usecase.LoadSongFromRemoteUseCase

class HomeViewModel(
    private val getPhotoUseCase: LoadSongFromRemoteUseCase
): ViewModel() {

}