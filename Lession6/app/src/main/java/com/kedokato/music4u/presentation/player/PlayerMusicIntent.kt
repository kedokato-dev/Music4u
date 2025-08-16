package com.kedokato.music4u.presentation.player

sealed class PlayerMusicIntent {

    data object OnPlayPauseClick : PlayerMusicIntent()

    data class OnNextClick(val songId: Long) : PlayerMusicIntent()

    data class OnPreviousClick(val songId: Long) : PlayerMusicIntent()

    data object OnRepeatClick : PlayerMusicIntent()

    data object OnShuffleClick : PlayerMusicIntent()

    data object OnStopSong: PlayerMusicIntent()

    data class OnSeekTo(val position: Float) : PlayerMusicIntent()
}


sealed class PlayerMusicEvent {

    data class ShowToast(val message: String) : PlayerMusicEvent()

    data class ShowError(val message: String) : PlayerMusicEvent()

    object NavigateBack : PlayerMusicEvent()

    object ClosePlayer : PlayerMusicEvent()
}