package com.kedokato.lession6.presentation.home.top_artist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.domain.model.ArtistInfo
import com.kedokato.lession6.domain.usecase.home.GetTopArtistsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TopArtistViewModel(
    private val getTopArtistsUseCase: GetTopArtistsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TopArtistDetailState())
    val state: StateFlow<TopArtistDetailState> = _state.asStateFlow()

    init {
        val initialArtists = savedStateHandle.get<List<ArtistInfo>>("artists")
        if (initialArtists != null) {
            _state.value = _state.value.copy(artists = initialArtists, isLoading = false)
        } else {
            loadTopArtists()
        }
    }

    fun loadTopArtists() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, isNetworkError = false)
            try {
                val artists = getTopArtistsUseCase()
                _state.value = _state.value.copy(
                    artists = artists,
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
        loadTopArtists()
    }
}