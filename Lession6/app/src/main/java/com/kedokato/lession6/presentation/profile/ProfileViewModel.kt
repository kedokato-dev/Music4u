package com.kedokato.lession6.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.lession6.domain.usecase.GetUserProfileUseCase
import com.kedokato.lession6.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserIdUseCaseShared: GetUserIdUseCaseShared,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _profileEvent = MutableSharedFlow<ProfileEvent>()
    val profileEvent = _profileEvent.asSharedFlow()

    private val _openGalleryEvent = MutableSharedFlow<Unit>()
    val openGalleryEvent = _openGalleryEvent.asSharedFlow()





    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.NameChanged -> {
                _state.value = _state.value.copy(
                    name = intent.name,
                    nameError = null
                )
            }

            is ProfileIntent.PhoneChanged -> {
                _state.value = _state.value.copy(
                    phone = intent.phone,
                    phoneError = null
                )
            }

            is ProfileIntent.UniversityChanged -> {
                _state.value = _state.value.copy(
                    university = intent.email,
                    universityError = null
                )
            }

            is ProfileIntent.DescriptionChanged -> {
                _state.value = _state.value.copy(
                    description = intent.description,
                    descriptionError = null
                )
            }

            ProfileIntent.Submit -> {
                viewModelScope.launch {
                    submitForm()
                }
            }

            ProfileIntent.IsEdit -> {
                _state.value = _state.value.copy(
                    isEdit = !_state.value.isEdit,
                    inputEnable = !_state.value.inputEnable,
                    isSubmitVisible = !_state.value.isSubmitVisible
                )
            }



            ProfileIntent.ShowDialog -> {
                viewModelScope.launch {
                    showDialog()
                }
            }

            is ProfileIntent.LoadUserData -> {
                viewModelScope.launch {
                    loadUserData(userId = getUserId())
                }
            }

            ProfileIntent.ChangeAvatar -> {
                viewModelScope.launch {
                    _openGalleryEvent.emit(Unit)
                }
            }


            is ProfileIntent.AvatarSelected -> {
                Log.d("ProfileViewModel", "Avatar selected: ${intent.uri}")
                _state.update {
                    it.copy(avatarUrl = intent.uri)
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

    private suspend fun submitForm() {
        if (validateForm()) {
            val row = withContext(Dispatchers.IO) {
                updateUserProfileUseCase.invoke(
                    getUserId(),
                    name = _state.value.name,
                    phone = _state.value.phone,
                    university = _state.value.university,
                    description = _state.value.description,
                    avatarUrl = _state.value.avatarUrl?.toString()
                )
            }
            if (row > 0) {
                _state.update {
                    it.copy(
                        isSubmitVisible = false,
                        inputEnable = false,
                        isEdit = !_state.value.isEdit
                    )
                }
            } else {
                Log.d("ProfileViewModel", "submitForm: Update failed")
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

    private suspend fun loadUserData(userId: Long) {
        val userProfile = withContext(Dispatchers.IO){
            getUserProfileUseCase(userId)
        }

        Log.d("ProfileViewModel", "loadUserData: $userProfile")

        if(userProfile!=null){
            _state.update {
                it.copy(
                    name = userProfile.name?: "",
                    phone = userProfile.phone?: "",
                    university = userProfile.university?: "",
                    description = userProfile.description?: "",
                    avatarUrl = userProfile.avatar?.toUri()
                )
            }
        } else {

        }
    }

    private suspend fun getUserId(): Long {
        return withContext(Dispatchers.IO) {
            getUserIdUseCaseShared() ?: 0L
        }
    }


}