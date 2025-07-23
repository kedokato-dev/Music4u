package com.kedokato.lession6

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kedokato.lession6.ui.theme.Green

@Composable
fun ProfileView() {
    ProfileContent()
}


@Composable
fun ProfileContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        ProfileTopBar("My Infomation", modifier = Modifier)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AvatarImage(modifier = Modifier)
        }

        Spacer(modifier = Modifier.size(16.dp))
        EditContainer()
        val showDialog = remember { mutableStateOf(false) }
        val painter = painterResource(id = R.drawable.succes)

        SubmitButton(
            title = "Submit",
            onClick = {
                showDialog.value = true
            }
        )

        if (showDialog.value) {
            DialogWithImage(
                onDismissRequest = { showDialog.value = false },
                painter = painter,
                imageDescription = "Avatar Image"
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(title: String, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title, color = Color.Black,
            )
        },
        colors = topAppBarColors(
            containerColor = Color(0x006200EE),
            titleContentColor = Color.White
        ),
        expandedHeight = TopAppBarDefaults.LargeAppBarCollapsedHeight,
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Settings Icon",
                modifier = Modifier.size(24.dp)
            )
        },
    )
}
@Composable
fun TextArea(
    value: String ,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String,
    line: Int = 1,
    height: Int = 50,
    singleLine: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hintText,
                color = Color.Gray,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = height.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        maxLines = line,
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            errorContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


@Composable
fun LabelForTextField(lable: String) {
    Text(
        text = lable,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun AvatarImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = "Avatar Image",
        modifier = modifier
            .size(150.dp)
    )
}

@Composable
fun EditContainer() {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                LabelForTextField("Name")
                val nameState = remember { mutableStateOf("") }
                TextArea(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    hintText = "Enter your name...",
                    modifier = Modifier.fillMaxWidth(),
                    line = 1
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                LabelForTextField("Phone")
                TextArea(
                    value = "",
                    onValueChange = {},
                    hintText = "Enter your phone...",
                    modifier = Modifier.fillMaxWidth(),
                    line = 1
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        LabelForTextField("University")
        TextArea(
            value = "",
            onValueChange = {},
            hintText = "Enter your university...",
            modifier = Modifier.fillMaxWidth(),
            line = 1
        )

        Spacer(modifier = Modifier.size(16.dp))

        LabelForTextField("describe yourself")
        TextArea(
            value = "",
            onValueChange = {},
            hintText = "Enter a short description about yourself...",
            modifier = Modifier.fillMaxWidth(),
            line = 5,
            height = 160,
            singleLine = false
        )

    }
}

@Composable
fun SubmitButton(title: String = "Submit", onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
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
    subTitle: String = "Your information has been updated!"
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
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
                    text= subTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,


                )
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun PreviewEditContainer() {
//    EditContainer()
//}

//@Preview
//@Composable
//fun PreviewTextArea() {
//    TextArea(modifier = Modifier, hintText = "Enter your text here")
//}

//@Preview(showBackground = true)
//@Composable
//fun PreviewAvatarImage() {
//    AvatarImage(modifier = Modifier)
//}
//

//@Preview
//@Composable
//fun PreviewProfileTopBar() {
//    ProfileTopBar(title = "Profile")
//}

//@Preview
//@Composable
//fun PreviewSubmitButton() {
//    DialogWithImage(
//        onDismissRequest = {},
//        painter = painterResource(id = R.drawable.succes),
//        imageDescription = "Avatar Image"
//    )
//}


@Preview(showBackground = true)
@Composable
fun PreviewProfileContent() {
    ProfileContent()
}

// chỉnh sửa giả vờ thôi để test PR