package com.kedokato.lession6.presentation.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    title: String,
    modifier: Modifier,
    isEdit: Boolean,
    onIconClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
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
           if(!isEdit){
               Icon(
                   painter = painterResource(id = R.drawable.edit_1),
                   contentDescription = "Settings Icon",
                   modifier = modifier
                       .size(24.dp)
                       .clickable {
                           onIconClick()
                       }
               )
           }
        },
        navigationIcon = {

            IconButton(
                onClick = onBackClick,
                modifier = modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.back
                    ),
                    contentDescription = "Back Icon",
                    modifier = modifier.size(24.dp)
                )
            }
        }
    )
}
