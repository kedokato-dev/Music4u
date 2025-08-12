package com.kedokato.lession6.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.MusicRepo
import com.kedokato.lession6.domain.usecase.music.NextSongUseCase
import com.kedokato.lession6.domain.usecase.music.PauseSongUseCase
import com.kedokato.lession6.domain.usecase.music.PlaySongUseCase
import com.kedokato.lession6.domain.usecase.music.PrevSongUseCase
import com.kedokato.lession6.domain.usecase.music.ResumeSongUseCase
import com.kedokato.lession6.domain.usecase.music.StopSongUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerMusicViewModel(
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val nextSongUseCase: NextSongUseCase,
    private val prevSongUseCase: PrevSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val stopSongUseCase: StopSongUseCase,
    private val musicRepo: MusicRepo
): ViewModel() {
    private val _playerMusicState = MutableStateFlow(PlayerMusicState())
    val playerMusicState: StateFlow<PlayerMusicState> = _playerMusicState.asStateFlow()

    init {
        viewModelScope.launch {
            musicRepo.getPlayerState().collect { serviceState ->
                // Đồng bộ trực tiếp từ service state
                _playerMusicState.value = _playerMusicState.value.copy(
                    isPlaying = serviceState.isPlaying,
                    currentPosition = serviceState.currentPosition,
                    duration = serviceState.duration,
                    progress = serviceState.progress,
                    song = serviceState.song,
                )
            }
        }
    }

    fun processIntent(intent: PlayerMusicIntent) {
        when (intent) {
            is PlayerMusicIntent.OnNextClick -> TODO()
            is PlayerMusicIntent.OnPlayPauseClick -> togglePlayPause()
            is PlayerMusicIntent.OnSeekTo -> seekTo(intent.position)
            is PlayerMusicIntent.OnPreviousClick -> TODO()
            is PlayerMusicIntent.OnRepeatClick -> TODO()
            is PlayerMusicIntent.OnShuffleClick -> TODO()
            PlayerMusicIntent.OnStopSong -> {
                onStop()
            }
        }
    }


    private fun onPlay(song: Song) {
        viewModelScope.launch {
            playSongUseCase(song)
        }
    }

    private fun onPause() {
        viewModelScope.launch {
            pauseSongUseCase()
        }
    }

    private fun onNext() {
        viewModelScope.launch {
            nextSongUseCase()
        }
    }

    private fun onPrev() {
        viewModelScope.launch {
            prevSongUseCase()
        }
    }

    private fun onResume(song: Song) {
        viewModelScope.launch {
            val currentSong = _playerMusicState.value.song
            if (currentSong != null && currentSong.id == song.id) {
                resumeSongUseCase()
            } else {
                playSongUseCase(song)
            }
            _playerMusicState.value = _playerMusicState.value.copy(isPlaying = true)
        }
    }

    private fun togglePlayPause() {
        val currentSong = _playerMusicState.value.song ?: return
        viewModelScope.launch {
            if (_playerMusicState.value.isPlaying) {
                pauseSongUseCase()
            } else {
                if (_playerMusicState.value.currentPosition > 0) {
                    resumeSongUseCase()
                } else {
                    playSongUseCase(currentSong)
                }
            }
        }
    }

    private fun seekTo(progress: Float) {
        viewModelScope.launch {
            val duration = _playerMusicState.value.duration
            if (duration > 0) {
                val position = (progress * duration).toLong()
                musicRepo.seekTo(position)
            }
        }
    }

    private fun onStop() {
        viewModelScope.launch {
            stopSongUseCase()
        }
    }


    fun setSong(song: Song) {
        _playerMusicState.value = _playerMusicState.value.copy(song = song)
    }

    fun playNewSong(song: Song) {
        viewModelScope.launch {
            playSongUseCase(song)
        }
    }
}