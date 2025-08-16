package com.kedokato.lession6.presentation.home.top_tracks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.domain.model.TrackInfo
import com.kedokato.lession6.domain.usecase.home.GetTopTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TopTracksViewModel(
    private val getTopTrackUseCase: GetTopTracksUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(TopTracksDetailState())
    val state: StateFlow<TopTracksDetailState> = _state.asStateFlow()

    init {
        // Kiểm tra xem danh sách albums đã được truyền qua tham số điều hướng chưa
        val initialTracks = savedStateHandle.get<List<TrackInfo>>("tracks")
        if (initialTracks != null) {
            _state.value = _state.value.copy(tracks = initialTracks, isLoading = false)
        } else {
            loadTracks()
        }
    }

    fun loadTracks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, isNetworkError = false)
            try {
                val tracks = getTopTrackUseCase()
                _state.value = _state.value.copy(
                    tracks = tracks,
                    isLoading = false,
                    error = null,
                    isNetworkError = false
                )
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Lỗi mạng: Vui lòng kiểm tra kết nối internet",
                    isNetworkError = true
                )
            } catch (e: HttpException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Lỗi server: ${e.code()}",
                    isNetworkError = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định",
                    isNetworkError = false
                )
            }
        }
    }

    fun retry() {
        loadTracks()
    }
}