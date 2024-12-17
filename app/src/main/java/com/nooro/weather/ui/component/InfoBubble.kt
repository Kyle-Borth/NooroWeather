package com.nooro.weather.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nooro.weather.ui.theme.NooroTheme
import com.nooro.weather.ui.theme.PaddingNormal

@Composable
fun InfoBubble(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun InfoBubblePreview() {
    NooroTheme {
        Box(modifier = Modifier.fillMaxWidth().padding(PaddingNormal)) {
            InfoBubble {
                Text(text = "This text is inside of an info bubble", modifier = Modifier.padding(PaddingNormal))
            }
        }
    }
}