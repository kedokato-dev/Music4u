package com.kedokato.lession6.presentation.player

sealed class PlayerMusicIntent {

    data object OnPlayPauseClick : PlayerMusicIntent()

    data class OnNextClick(val songId: String) : PlayerMusicIntent()

    data class OnPreviousClick(val songId: String) : PlayerMusicIntent()

    data class OnRepeatClick(val isRepeatMode: Boolean) : PlayerMusicIntent()

    data class OnShuffleClick(val isShuffle: Boolean) : PlayerMusicIntent()

    data object OnStopSong: PlayerMusicIntent()

    data class OnSeekTo(val position: Float) : PlayerMusicIntent()
}


sealed class PlayerMusicEvent {

    data class ShowToast(val message: String) : PlayerMusicEvent()

    data class ShowError(val message: String) : PlayerMusicEvent()

    object NavigateBack : PlayerMusicEvent()

    object ClosePlayer : PlayerMusicEvent()
}