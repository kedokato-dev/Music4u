package com.kedokato.lession6.presentation.player

sealed class PlayerMusicIntent {

    data class OnPlayPauseClick(val isPlaying: Boolean) : PlayerMusicIntent()

    data class OnNextClick(val songId: String) : PlayerMusicIntent()

    data class OnPreviousClick(val songId: String) : PlayerMusicIntent()

    data class OnRepeatClick(val isRepeatMode: Boolean) : PlayerMusicIntent()

    data class OnShuffleClick(val isShuffle: Boolean) : PlayerMusicIntent()

    data class OnSeekTo(val position: Long) : PlayerMusicIntent()
}


sealed class PlayerMusicEvent {

    data class ShowToast(val message: String) : PlayerMusicEvent()

    data class ShowError(val message: String) : PlayerMusicEvent()

    object NavigateBack : PlayerMusicEvent()

    object ClosePlayer : PlayerMusicEvent()
}