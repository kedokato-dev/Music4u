package com.kedokato.lession6.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionManager {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> get() = _userId

    fun setUserId(id: Long) {
        _userId.value = id
    }

    fun clear() {
        _userId.value = null
    }
}