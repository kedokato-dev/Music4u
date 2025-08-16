package com.kedokato.music4u.presentation.home.top_albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.music4u.domain.model.AlbumInfo
import com.kedokato.music4u.domain.usecase.home.GetTopAlbumsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TopAlbumsViewModel(
    private val getTopAlbumsUseCase: GetTopAlbumsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TopAlbumsDetailState())
    val state: StateFlow<TopAlbumsDetailState> = _state.asStateFlow()

    init {
        // Kiểm tra xem danh sách albums đã được truyền qua tham số điều hướng chưa
        val initialAlbums = savedStateHandle.get<List<AlbumInfo>>("albums")
        if (initialAlbums != null) {
            _state.value = _state.value.copy(albums = initialAlbums, isLoading = false)
        } else {
            loadTopAlbums()
        }
    }

    fun loadTopAlbums() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, isNetworkError = false)
            try {
                val albums = getTopAlbumsUseCase()
                _state.value = _state.value.copy(
                    albums = albums,
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
        loadTopAlbums()
    }
}