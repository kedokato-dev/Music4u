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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.component.Button
import com.kedokato.lession6.component.DialogWithImage
import com.kedokato.lession6.component.TextArea
import kotlinx.coroutines.delay


@Composable
fun ProfileView(
    viewModel: ProfileViewModel = viewModel(),
    isEditMode: Boolean = false,
    onEditModeChange: (Boolean) -> Unit = {},
    isDarkTheme: Boolean = false,
    modifier: Modifier
) {
    val colorScheme = getCurrentColorScheme()
    val state by viewModel.state.collectAsState()

    ProfileContent(
        isEditMode = isEditMode,
        onEditModeChange = onEditModeChange,
        isDarkTheme = isDarkTheme,
        modifier,
        state = state
    )
}

@Composable
fun ProfileContent(
    isEditMode: Boolean = false,
    onEditModeChange: (Boolean) -> Unit = {},
    isDarkTheme: Boolean = false,
    modifier: Modifier,
    state: ProfileState
) {
    val focusManager = LocalFocusManager.current
    val colorScheme = getCurrentColorScheme(isDarkTheme)

//    var name by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var university by remember { mutableStateOf("") }
//    var describe by remember { mutableStateOf("") }
//
//    var nameError by remember { mutableStateOf<String?>(null) }
//    var phoneError by remember { mutableStateOf<String?>(null) }
//    var universityError by remember { mutableStateOf<String?>(null) }

    val showDialog = remember { mutableStateOf(false) }
    val painter = painterResource(id = R.drawable.succes)

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        imageUri = uri
    }


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
            onIconClick = { onEditModeChange(!isEditMode) },
            onThemeToggle = { /* Handle theme toggle */ },
            isDarkTheme = isDarkTheme
        )

        AvatarImage(
            modifier = modifier,
            colorScheme = colorScheme,
            imageUri = imageUri,
            onClickCamera = {
                launcher.launch(arrayOf("image/*"))

            }
        )

        Spacer(modifier = modifier.size(16.dp))

        EditContainer(
            name = state.name,
            onNameChange = {

            },
            phone = phone,
            onPhoneChange = { phone = it },
            university = university,
            onUniversityChange = { university = it },
            describe = describe,
            onDescribeChange = { describe = it },
            nameError = nameError,
            phoneError = phoneError,
            universityError = universityError,
            isEnale = isEditMode,
            colorScheme = colorScheme
        )

        if (isEditMode) {
          Button(
              text = "Submit",
              modifier = Modifier.fillMaxWidth(0.3f),
              onClick ={
                  nameError =
                        if (name.isBlank() || !name.matches(Regex("^[\\p{L} ]*\$"))) "Name is invalid" else null
                    phoneError =
                        if (!phone.matches(Regex("^\\d{10}$"))) "Phone is invalid" else null
                    universityError =
                        if (university.isBlank() || !university.matches(Regex("^[\\p{L} ]*\$"))) "University is invalid" else null

                    if (nameError == null && phoneError == null && universityError == null) {
                        showDialog.value = true
                        onEditModeChange(false)
                    }
              })
        }

        if (showDialog.value) {
            DialogWithImage(
                onDismissRequest = { showDialog.value = false },
                painter = painter,
                imageDescription = "Avatar Image",
                colorScheme = colorScheme,
            )

            AnimatedVisibility(
                visible = showDialog.value,
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
                    onDismissRequest = { showDialog.value = false },
                    painter = painter,
                    imageDescription = "Avatar Image"
                )
            }

            LaunchedEffect(showDialog.value) {
                if (showDialog.value) {
                    delay(2000)
                    showDialog.value = false
                }
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
    val colorScheme = getCurrentColorScheme(isDarkTheme)
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
    lable: String,
    colorScheme: ColorScheme
) {
    Text(
        text = lable,
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
    Box(){
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
        ){
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
    isEnale: Boolean = true,
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
                    isEnable = isEnale,
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
                    isEnable = isEnale,
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
            isEnable = isEnale,
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
            isEnable = isEnale,
            colorScheme = colorScheme
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileContent() {
    ProfileContent(
        isEditMode = true,
        onEditModeChange = {},
        isDarkTheme = false,
        modifier = Modifier.fillMaxSize()
    )
}