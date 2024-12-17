package com.nooro.weather.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.nooro.weather.R
import com.nooro.weather.ui.theme.NooroTheme
import com.nooro.weather.ui.theme.PaddingNormal

//TODO Should create my own color class for this text field
@Composable
fun NooroTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.primary,
        unfocusedTextColor = MaterialTheme.colorScheme.primary,
        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        shape = MaterialTheme.shapes.medium,
        colors = colors
    )
}

@Preview(showBackground = true)
@Composable
private fun NooroTextFieldPreview() {
    NooroTheme {
        Column(modifier = Modifier.padding(PaddingNormal), verticalArrangement = Arrangement.spacedBy(PaddingNormal)) {
            NooroTextField(
                value = TextFieldValue(),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Placeholder Text") },
                trailingIcon = { Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null) }
            )

            NooroTextField(
                value = TextFieldValue(text = "Inputted Text"),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Placeholder Text") },
                trailingIcon = { Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null) }
            )
        }
    }
}