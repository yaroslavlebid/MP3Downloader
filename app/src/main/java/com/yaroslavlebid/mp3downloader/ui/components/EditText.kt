package com.yaroslavlebid.mp3downloader.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaroslavlebid.mp3downloader.R
import com.yaroslavlebid.mp3downloader.ui.theme.LightMidGray
import com.yaroslavlebid.mp3downloader.ui.theme.MP3DownloaderTheme
import com.yaroslavlebid.mp3downloader.ui.theme.MidGray

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = { },
    labelText: String? = null,
    helperText: String? = null,
    placeholderText: String = "",
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onFieldClick: () -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val source = remember {
        MutableInteractionSource()
    }

    if (source.collectIsPressedAsState().value) {
        onFieldClick()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (labelText != null) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(bottom = 14.dp)
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = MaterialTheme.typography.h3,
            singleLine = true,
            maxLines = 1,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onBackground,
                disabledTextColor = MaterialTheme.colors.onSecondary,
                focusedBorderColor = MidGray,
                unfocusedBorderColor = MidGray,
                disabledBorderColor = LightMidGray,
                leadingIconColor = MaterialTheme.colors.onBackground,
                disabledLeadingIconColor = MaterialTheme.colors.onSecondary,
                trailingIconColor = MaterialTheme.colors.onBackground,
                disabledTrailingIconColor = MaterialTheme.colors.onSecondary,
                backgroundColor = MaterialTheme.colors.secondary,
                cursorColor = MaterialTheme.colors.onBackground
            ),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onSecondary
                )
            },
            interactionSource = source
        )
        if (helperText != null) {
            InfoText(
                text = helperText,
                leadingIconId = R.drawable.info_icon,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditTextPreview() {
    MP3DownloaderTheme {
        EditText(
            value = "",
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.url_icon), contentDescription = "")
            },
            trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.close_icon), contentDescription = "")
            },
            labelText = "YouTube Link",
            helperText = "Where you want to save the MP3",
            placeholderText = "Enter a url"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditTextPreviewDisabled() {
    MP3DownloaderTheme {
        EditText(
            value = "https://www.youtu…XCScZ3Q",
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.url_icon), contentDescription = "")
            },
            trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.close_icon), contentDescription = "")
            },
            enabled = false,
            labelText = "YouTube Link"
        )
    }
}