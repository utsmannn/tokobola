package com.utsman.tokobola.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun ErrorScreen(throwable: Throwable) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val painter = painterResource(SharedRes.images.icon_emoji_error)
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = throwable.message.orEmpty(),
            color = Color.Red.copy(alpha = 0.7f),
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(22.dp)
        )
    }
}

@Composable
fun SimpleErrorScreen(throwable: Throwable) {
    Row(
        modifier = Modifier.fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = painterResource(SharedRes.images.icon_emoji_error)
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = throwable.message.orEmpty(),
            color = Color.Red.copy(alpha = 0.7f),
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(22.dp)
        )
    }
}