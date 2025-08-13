package com.kedokato.lession6.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.data.mapper.UserMapper
import com.kedokato.lession6.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.lession6.domain.usecase.GetUserProfileUseCase
import com.kedokato.lession6.domain.usecase.home.GetTopAlbumsUseCase
import com.kedokato.lession6.domain.usecase.home.GetTopArtistsUseCase
import com.kedokato.lession6.domain.usecase.home.GetTopTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getTopAlbumsUseCase: GetTopAlbumsUseCase,
    private val getTopTracksUseCase: GetTopTracksUseCase,
    private val getTopArtistsUseCase: GetTopArtistsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserIdUseCase: GetUserIdUseCaseShared,
): ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadTopAlbums -> {
                viewModelScope.launch {
                    _homeState.value = _homeState.value.copy(isLoading = true, error = null)
                    try {
                        val albums = getTopAlbumsUseCase()
                        _homeState.value = _homeState.value.copy(
                            albums = albums,
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        _homeState.value = _homeState.value.copy(
                            isLoading = false,
                            error = e.message ?: "An error occurred"
                        )
                    }
                }
            }
            HomeIntent.LoadTopTracks -> {
                viewModelScope.launch {
                    _homeState.value = _homeState.value.copy(isLoading = true, error = null)
                    try {
                        val tracks = getTopTracksUseCase()
                        _homeState.value = _homeState.value.copy(
                            tracks = tracks,
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        _homeState.value = _homeState.value.copy(
                            isLoading = false,
                            error = e.message ?: "An error occurred"
                        )
                    }
                }
            }

            HomeIntent.LoadTopArtists -> {
                viewModelScope.launch {
                    _homeState.value = _homeState.value.copy(isLoading = true, error = null)
                    try {
                        val artists = getTopArtistsUseCase()
                        _homeState.value = _homeState.value.copy(
                            artists = artists,
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        _homeState.value = _homeState.value.copy(
                            isLoading = false,
                            error = e.message ?: "An error occurred"
                        )
                    }
                }
            }

            HomeIntent.LoadUserProfile -> {
                viewModelScope.launch {
                    _homeState.value = _homeState.value.copy(isLoading = true, error = null)
                    try {
                        val userId = getUserId()
                        val userProfile = getUserProfileUseCase(userId)
                        _homeState.value = _homeState.value.copy(
                            userProfile = UserMapper.toDomain(userProfile),
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        _homeState.value = _homeState.value.copy(
                            isLoading = false,
                            error = e.message ?: "An error occurred"
                        )
                    }
                }
            }
        }
    }

    private suspend fun getTopAlbums() {
       getTopAlbumsUseCase()
    }

    private suspend fun getUserId(): Long {
        return withContext(Dispatchers.IO) {
            getUserIdUseCase() ?: 0L
        }
    }



}