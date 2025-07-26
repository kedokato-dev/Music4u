package com.kedokato.lession6

import android.R.attr.contentDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.compose.getCurrentColorScheme
import kotlinx.coroutines.delay

@Composable
fun ProfileView(
    isEditMode: Boolean = false,
    onEditModeChange: (Boolean) -> Unit = {},
    isDarkTheme: Boolean = false,
    modifier: Modifier
) {
    ProfileContent(
        isEditMode = isEditMode,
        onEditModeChange = onEditModeChange,
        isDarkTheme = isDarkTheme,
        modifier
    )
}

@Composable
fun ProfileContent(
    isEditMode: Boolean = false,
    onEditModeChange: (Boolean) -> Unit = {},
    isDarkTheme: Boolean = false,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val colorScheme = getCurrentColorScheme(isDarkTheme)

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }
    var describe by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var universityError by remember { mutableStateOf<String?>(null) }

    val showDialog = remember { mutableStateOf(false) }
    val painter = painterResource(id = R.drawable.succes)

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
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AvatarImage(modifier = modifier, colorScheme)
        }

        Spacer(modifier = modifier.size(16.dp))

        EditContainer(
            name = name,
            onNameChange = { name = it },
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
            SubmitButton(
                title = "Submit",
                onClick = {
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
                },
                colorScheme = colorScheme
            )
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
                enter = androidx.compose.animation.fadeIn(
                    animationSpec = tween(300)
                ) + androidx.compose.animation.scaleIn(
                    initialScale = 0.5f,
                    animationSpec = tween(300)
                ),
                exit = fadeOut(
                    animationSpec = tween(300)
                ) + androidx.compose.animation.scaleOut(
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
fun TextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String,
    line: Int = 1,
    height: Int = 50,
    singleLine: Boolean = true,
    isError: Boolean = false,
    isEnable: Boolean = false,
    colorScheme: ColorScheme
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hintText,
                color = colorScheme.onSurfaceVariant,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = height.dp)
            .border(
                width = 1.dp,
                color = colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        maxLines = line,
        singleLine = singleLine,
        isError = isError,
        enabled = isEnable,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.onSecondary,
            unfocusedContainerColor = colorScheme.onSecondary,
            disabledContainerColor = colorScheme.onSecondary,
            errorContainerColor = colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = colorScheme.primary,
            unfocusedTextColor = colorScheme.primary,
            disabledTextColor = colorScheme.primary,
        )
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
    colorScheme: ColorScheme
) {
    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = "Avatar Image",
        modifier = modifier
            .size(150.dp)
            .border(
                width = 2.dp,
                color = colorScheme.primary,
                shape = RoundedCornerShape(75.dp)
            )
    )
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

@Composable
fun SubmitButton(title: String = "Submit", onClick: () -> Unit = {}, colorScheme: ColorScheme) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.surfaceTint,
            contentColor = colorScheme.onSecondary
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
    ) {
        Text(text = title)
    }
}

@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    painter: Painter,
    imageDescription: String,
    title: String = "Success!",
    subTitle: String = "Your information has been updated!",
    colorScheme: ColorScheme = MaterialTheme.colorScheme
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painter,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(100.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    color = Green,
                    modifier = Modifier.padding(16.dp),
                )

                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
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