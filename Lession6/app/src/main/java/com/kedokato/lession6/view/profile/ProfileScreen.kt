package com.kedokato.lession6.view.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.component.Button
import com.kedokato.lession6.component.DialogWithImage
import com.kedokato.lession6.component.TextArea
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow


@Composable
fun ProfileView(
    viewModel: ProfileViewModel = viewModel(),
    isEditMode: Boolean = false,
    isDarkTheme: Boolean = false,
    modifier: Modifier
) {
    val state by viewModel.state.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        viewModel.onAvatarSelected(uri)
    }


    LaunchedEffect(Unit) {
        viewModel.openGalleryEvent.collect {
            launcher.launch(arrayOf("image/*"))
        }
    }



    ProfileContent(
        isEditMode = state.isSubmitVisible,
        isDarkTheme = isDarkTheme,
        modifier,
        state = state,
        viewModel = viewModel

    )
}

@Composable
fun ProfileContent(
    isEditMode: Boolean = false,
    isDarkTheme: Boolean = false,
    modifier: Modifier,
    state: ProfileState,
    viewModel: ProfileViewModel
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
            onIconClick = { viewModel.processIntent(ProflieIntent.isEdit) },
            onThemeToggle = { /* Handle theme toggle */ },
            isDarkTheme = isDarkTheme
        )

        AvatarImage(
            modifier = modifier,
            colorScheme = colorScheme,
            imageUri = state.avatarUrl,
            onClickCamera = {
                viewModel.processIntent(ProflieIntent.ChangeAvatar)

            }
        )

        Spacer(modifier = modifier.size(16.dp))

        EditContainer(
            name = state.name,
            onNameChange = {
                viewModel.processIntent(
                    ProflieIntent.NameChanged(it)
                )
            },
            phone = state.phone,
            onPhoneChange = { viewModel.processIntent(ProflieIntent.PhoneChanged(it)) },
            university = state.university,
            onUniversityChange = { viewModel.processIntent(ProflieIntent.UniversityChanged(it)) },
            describe = state.description,
            onDescribeChange = { viewModel.processIntent(ProflieIntent.DescriptionChanged(it)) },
            nameError = state.nameError,
            phoneError = state.phone,
            universityError = state.universityError,
            isEnable = state.inputEnable,
            colorScheme = colorScheme
        )

        if (state.isEdit) {
            Button(
                text = "Submit",
                modifier = Modifier.fillMaxWidth(0.3f),
                onClick = {
                    viewModel.processIntent(ProflieIntent.Submit)
                        // thay doi state cho dialog
                    viewModel.processIntent(ProflieIntent.ShowDialog)
                })
        }

        if (state.showDialog) {
            DialogWithImage(
                onDismissRequest = { viewModel.processIntent(ProflieIntent.ShowDialog) },
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
                    onDismissRequest = {viewModel.processIntent(ProflieIntent.ShowDialog) },
                    painter = painterResource(R.drawable.succes),
                    imageDescription = "Avatar Image"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    title: String,
    modifier: Modifier,
    isEdit: Boolean,
    onIconClick: () -> Unit = {},
    onThemeToggle: () -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    val colorScheme = getCurrentColorScheme()
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = colorScheme.primary,
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.background,
            titleContentColor = colorScheme.primary,
            actionIconContentColor = colorScheme.primary,
            navigationIconContentColor = colorScheme.primary,
            scrolledContainerColor = colorScheme.background,
        ),
        expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight,
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Settings Icon",
                modifier = modifier
                    .size(24.dp)
                    .clickable {
                        onIconClick()
                    }
            )
        },
        navigationIcon = {
            isDarkTheme.let { darkTheme ->
                Icon(
                    painter = painterResource(id = if (darkTheme) R.drawable.light_mode_1 else R.drawable.dark_mode_1),
                    contentDescription = "Theme Toggle Icon",
                    modifier = modifier
                        .size(24.dp)
                        .clickable {
                            onThemeToggle()
                        }
                )
            }
        }
    )
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

@Composable
fun AvatarImage(
    modifier: Modifier,
    colorScheme: ColorScheme,
    imageUri: Uri? = null,
    onClickCamera: () -> Unit = {}
) {
    Box() {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Avatar Image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(150.dp)
                    .border(
                        width = 2.dp,
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(150.dp)
                    )
                    .clip(RoundedCornerShape(150.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Avatar Image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(150.dp)
                    .border(
                        width = 2.dp,
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(150.dp)
                    )
                    .clip(RoundedCornerShape(150.dp))
            )
        }

        Box(
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .background(
                    color = colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Camera Icon",
                modifier = modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd)
                    .padding(8.dp)
                    .background(
                        color = colorScheme.surface,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        onClickCamera()
                    },
                tint = colorScheme.primary
            )
        }
    }
}

@Composable
fun EditContainer(
    name: String,
    onNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    university: String,
    onUniversityChange: (String) -> Unit,
    describe: String,
    onDescribeChange: (String) -> Unit,
    nameError: String? = null,
    phoneError: String? = null,
    universityError: String? = null,
    isEnable: Boolean = true,
    colorScheme: ColorScheme
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                LabelForTextField("Name", colorScheme = colorScheme)
                TextArea(
                    value = name,
                    onValueChange = onNameChange,
                    hintText = "Enter your name...",
                    modifier = Modifier.fillMaxWidth(),
                    line = 1,
                    isError = nameError != null,
                    isEnable = isEnable,
                    colorScheme = colorScheme
                )
                if (nameError != null) {
                    Text(
                        text = nameError,
                        color = colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                LabelForTextField("Phone", colorScheme = colorScheme)
                TextArea(
                    value = phone,
                    onValueChange = onPhoneChange,
                    hintText = "Enter your phone...",
                    modifier = Modifier.fillMaxWidth(),
                    line = 1,
                    isError = phoneError != null,
                    isEnable = isEnable,
                    colorScheme = colorScheme
                )
                if (phoneError != null) {
                    Text(
                        text = phoneError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        LabelForTextField("University", colorScheme = colorScheme)
        TextArea(
            value = university,
            onValueChange = onUniversityChange,
            hintText = "Enter your university...",
            modifier = Modifier.fillMaxWidth(),
            line = 1,
            isError = universityError != null,
            isEnable = isEnable,
            colorScheme = colorScheme
        )
        if (universityError != null) {
            Text(
                text = universityError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        LabelForTextField("Describe yourself", colorScheme = colorScheme)
        TextArea(
            value = describe,
            onValueChange = onDescribeChange,
            hintText = "Enter a short description about yourself...",
            modifier = Modifier.fillMaxWidth(),
            line = 5,
            height = 160,
            singleLine = false,
            isEnable = isEnable,
            colorScheme = colorScheme
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileContent() {
    val viewModel: ProfileViewModel = viewModel()
    ProfileContent(
        isEditMode = true,
        isDarkTheme = false,
        modifier = Modifier.fillMaxSize(),
        state = ProfileState(
            name = "John Doe",
            phone = "1234567890",
            university = "Example University",
            description = "A short description about John Doe.",
            nameError = null,
            phoneError = null,
            universityError = null,
            isSubmitVisible = true,
            inputEnable = true,
            isEdit = true,
            avatarUrl = null,
            showDialog = false
        ),
        viewModel = viewModel
    )
}
