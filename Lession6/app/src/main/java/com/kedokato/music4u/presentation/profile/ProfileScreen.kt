package com.kedokato.music4u.presentation.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R
import com.kedokato.music4u.presentation.component.Button
import com.kedokato.music4u.presentation.component.ButtonLogout
import com.kedokato.music4u.presentation.component.DialogWithImage
import com.kedokato.music4u.presentation.profile.component.AvatarImage
import com.kedokato.music4u.presentation.profile.component.EditContainer
import com.kedokato.music4u.presentation.profile.component.ProfileTopBar
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProfileScreen(
    isEditMode: Boolean = false,
    isDarkTheme: Boolean = false,
    modifier: Modifier,
    onBackClick: () -> Unit = {},
    onNavigationLogin: () -> Unit = {},
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            viewModel.processIntent(ProfileIntent.AvatarSelected(it))
        }

    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(ProfileIntent.LoadUserData)
    }

    LaunchedEffect(Unit) {
        viewModel.openGalleryEvent.collect {
            launcher.launch(arrayOf("image/*"))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.profileEvent.collect { event ->
            when (event) {
                is ProfileEvent.OnBackPressed -> {
                    onBackClick()
                }

                ProfileEvent.ChangeAvatar -> TODO()
                ProfileEvent.ShowDialog -> TODO()
                ProfileEvent.ToggleEditMode -> TODO()
                ProfileEvent.ShowSnackBarError -> TODO()
                ProfileEvent.OpenGallery -> TODO()
                ProfileEvent.Logout -> {
                    onNavigationLogin()
                }
            }
        }
    }


    ProfileContent(
        isEditMode = state.isSubmitVisible,
        isDarkTheme = isDarkTheme,
        modifier,
        state = state,
        viewModel = viewModel,
        onBackClick = onBackClick,
        onNavigationLogin = onNavigationLogin

    )
}

@Composable
fun ProfileContent(
    isEditMode: Boolean = false,
    isDarkTheme: Boolean = false,
    modifier: Modifier,
    state: ProfileState,
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit = {},
    onNavigationLogin: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val colorScheme = getCurrentColorScheme()



    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        ProfileTopBar(
            title = "Profile",
            modifier = modifier,
            isEdit = isEditMode,
            onIconClick = { viewModel.processIntent(ProfileIntent.IsEdit) },
            onBackClick = {
                onBackClick()
            },
            isDarkTheme = isDarkTheme
        )

        AvatarImage(
            modifier = modifier,
            colorScheme = colorScheme,
            imageUri = state.avatarUrl,
            onClickCamera = {
                viewModel.processIntent(ProfileIntent.ChangeAvatar)

            },
            isEditMode = state.isEdit
        )

        Spacer(modifier = modifier.size(16.dp))

        EditContainer(
            name = state.name,
            onNameChange = {
                viewModel.processIntent(
                    ProfileIntent.NameChanged(it)
                )
            },
            phone = state.phone,
            onPhoneChange = { viewModel.processIntent(ProfileIntent.PhoneChanged(it)) },
            university = state.university,
            onUniversityChange = { viewModel.processIntent(ProfileIntent.UniversityChanged(it)) },
            describe = state.description,
            onDescribeChange = { viewModel.processIntent(ProfileIntent.DescriptionChanged(it)) },
            nameError = state.nameError,
            phoneError = state.phoneError,
            universityError = state.universityError,
            isEnable = state.inputEnable,
            colorScheme = colorScheme
        )

        if (state.isEdit) {
            Button(
                text = "Submit",
                modifier = Modifier.fillMaxWidth(0.3f),
                onClick = {
                    viewModel.processIntent(ProfileIntent.Submit)
                    // thay doi state cho dialog
                    viewModel.processIntent(ProfileIntent.ShowDialog)
                })
        }else{
            ButtonLogout(
                text = "Logout",
                modifier = Modifier.fillMaxWidth(0.3f),
                onClick = {
                    onNavigationLogin()
                })
        }

        if (state.showDialog) {
            DialogWithImage(
                onDismissRequest = { viewModel.processIntent(ProfileIntent.ShowDialog) },
                painter = painterResource(R.drawable.succes),
                imageDescription = "Avatar Image",
                colorScheme = colorScheme,
            )

            AnimatedVisibility(
                visible = state.showDialog,
                enter = fadeIn(
                    animationSpec = tween(300)
                ) + scaleIn(
                    initialScale = 0.5f,
                    animationSpec = tween(300)
                ),
                exit = fadeOut(
                    animationSpec = tween(300)
                ) + scaleOut(
                    targetScale = 0.5f,
                    animationSpec = tween(300)
                )
            ) {
                DialogWithImage(
                    onDismissRequest = { viewModel.processIntent(ProfileIntent.ShowDialog) },
                    painter = painterResource(R.drawable.succes),
                    imageDescription = "Avatar Image"
                )
            }
        }
    }
}

@Composable
fun LabelForTextField(
    label: String,
    colorScheme: ColorScheme
) {
    Text(
        text = label,
        color = colorScheme.primary,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

