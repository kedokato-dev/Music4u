package com.kedokato.music4u.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kedokato.music4u.presentation.component.TextArea
import com.kedokato.music4u.presentation.profile.LabelForTextField


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
                        modifier = Modifier.padding(top = 8.dp)
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
                        color = colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
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
                modifier = Modifier.padding(top = 8.dp)
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