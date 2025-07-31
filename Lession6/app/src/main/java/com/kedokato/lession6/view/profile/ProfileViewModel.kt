package com.kedokato.lession6.view.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()
    
    private val _openGalleryEvent = Channel<Unit>(Channel.BUFFERED)
    val openGalleryEvent = _openGalleryEvent.receiveAsFlow()

    fun onChangeAvatarClicked() {
        viewModelScope.launch {
            _openGalleryEvent.send(Unit)
        }
    }

    fun onAvatarSelected(uri: Uri?) {
        _state.update { it.copy(avatarUrl = uri) }
    }


    fun processIntent(intent: ProflieIntent) {
        when (intent) {
            is ProflieIntent.NameChanged -> {
                _state.value = _state.value.copy(
                    name = intent.name,
                    nameError = null
                )
            }

            is ProflieIntent.PhoneChanged -> {
                _state.value = _state.value.copy(
                    phone = intent.phone,
                    phoneError = null
                )
            }

            is ProflieIntent.UniversityChanged -> {
                _state.value = _state.value.copy(
                    university = intent.email,
                    universityError = null
                )
            }

            is ProflieIntent.DescriptionChanged -> {
                _state.value = _state.value.copy(
                    description = intent.description,
                    descriptionError = null
                )
            }

            ProflieIntent.Submit -> {
                submitForm()
            }

            ProflieIntent.isEdit -> {
                _state.value = _state.value.copy(
                    isEdit = !_state.value.isEdit,
                    inputEnable = !_state.value.inputEnable,
                    isSubmitVisible = !_state.value.isSubmitVisible
                )
            }

            ProflieIntent.onBackPressed -> {
                // Handle back press logic here
            }

            ProflieIntent.ChangeAvatar -> {
                onChangeAvatarClicked()
            }

            ProflieIntent.ShowDialog -> {
                viewModelScope.launch {
                    showDialog()
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        val currentState = _state.value
        var isValid = true

        var nameError: String? = null
        var phoneError: String? = null
        var universityError: String? = null

        if (currentState.name.isEmpty()) {
            nameError = "Name cannot be empty"
            isValid = false
        } else if (!validateName(currentState.name)) {
            nameError = "Invalid name format"
            isValid = false
        }

        if (currentState.phone.isEmpty()) {
            phoneError = "Phone cannot be empty"
            isValid = false
        } else if (!validatePhone(currentState.phone)) {
            phoneError = "Invalid phone format"
            isValid = false
        }

        if (currentState.university.isEmpty()) {
            universityError = "University cannot be empty"
            isValid = false
        } else if (!validateUniversity(currentState.university)) {
            universityError = "Invalid university format"
            isValid = false
        }


        _state.update {
            it.copy(
                nameError = nameError,
                phoneError = phoneError,
                universityError = universityError,
                descriptionError = null
            )
        }

        return isValid
    }


    private fun validateName(name: String): Boolean {
        val nameRegex = Regex("^[\\p{L} ]*\$")
        return nameRegex.matches(name)
    }

    private fun validatePhone(phone: String): Boolean {
        val phoneRegex = Regex("^\\d{10}\$")
        return phoneRegex.matches(phone)
    }

    private fun validateUniversity(university: String): Boolean {
        val universityRegex = Regex("^[\\p{L} ]*\$")
        return universityRegex.matches(university)
    }

    private fun submitForm() {
        if (validateForm()) {
            _state.update {
                it.copy(
                    isSubmitVisible = false,
                    inputEnable = false,
                    isEdit = !_state.value.isEdit
                )
            }

        } else {
            _state.update { it.copy(isSubmitVisible = true) }
        }
    }

    private suspend fun showDialog() {
        if(_state.value.nameError == null && _state.value.phoneError == null && _state.value.universityError == null){
            _state.update { it.copy(showDialog = true) }
            delay(2000)
            _state.update { it.copy(showDialog = false) }
        }

    }

}