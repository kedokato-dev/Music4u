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
import retrofit2.HttpException
import java.io.IOException

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
            HomeIntent.LoadTopAlbums -> loadTopAlbums()
            HomeIntent.LoadTopTracks -> loadTopTracks()
            HomeIntent.LoadTopArtists -> loadTopArtists()
            HomeIntent.LoadUserProfile -> loadUserProfile()
            HomeIntent.RetryLoading -> retryLoading()
        }
    }

    private fun loadTopAlbums() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true, error = null, isNetworkError = false)
            try {
                val albums = getTopAlbumsUseCase()
                _homeState.value = _homeState.value.copy(
                    albums = albums,
                    isLoading = false,
                    error = null,
                    isNetworkError = false
                )
            } catch (e: IOException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi mạng: Vui lòng kiểm tra kết nối internet",
                    isNetworkError = true
                )
            } catch (e: HttpException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi server: ${e.code()}",
                    isNetworkError = false
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định",
                    isNetworkError = false
                )
            }
        }
    }

    private fun loadTopTracks() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true, error = null, isNetworkError = false)
            try {
                val tracks = getTopTracksUseCase()
                _homeState.value = _homeState.value.copy(
                    tracks = tracks,
                    isLoading = false,
                    error = null,
                    isNetworkError = false
                )
            } catch (e: IOException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi mạng: Vui lòng kiểm tra kết nối internet",
                    isNetworkError = true
                )
            } catch (e: HttpException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi server: ${e.code()}",
                    isNetworkError = false
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định",
                    isNetworkError = false
                )
            }
        }
    }

    private fun loadTopArtists() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true, error = null, isNetworkError = false)
            try {
                val artists = getTopArtistsUseCase()
                _homeState.value = _homeState.value.copy(
                    artists = artists,
                    isLoading = false,
                    error = null,
                    isNetworkError = false
                )
            } catch (e: IOException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi mạng: Vui lòng kiểm tra kết nối internet",
                    isNetworkError = true
                )
            } catch (e: HttpException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi server: ${e.code()}",
                    isNetworkError = false
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định",
                    isNetworkError = false
                )
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true, error = null, isNetworkError = false)
            try {
                val userId = getUserId()
                val userProfile = getUserProfileUseCase(userId)
                _homeState.value = _homeState.value.copy(
                    userProfile = UserMapper.toDomain(userProfile),
                    isLoading = false,
                    error = null,
                    isNetworkError = false
                )
            } catch (e: IOException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi mạng: Vui lòng kiểm tra kết nối internet",
                    isNetworkError = true
                )
            } catch (e: HttpException) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = "Lỗi server: ${e.code()}",
                    isNetworkError = false
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định",
                    isNetworkError = false
                )
            }
        }
    }

    private fun retryLoading() {
        loadTopAlbums()
        loadTopTracks()
        loadTopArtists()
        loadUserProfile()
    }

    private suspend fun getUserId(): Long {
        return withContext(Dispatchers.IO) {
            getUserIdUseCase() ?: 0L
        }
    }
}