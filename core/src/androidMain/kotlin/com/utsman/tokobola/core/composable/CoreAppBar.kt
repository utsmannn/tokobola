package com.utsman.tokobola.core.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
actual fun CoreAppBar(
    title: String,
    previousTitle: String?,
    action: () -> Unit
) {
    TopAppBar(
        title = {
            AppText(
                text = title,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            Image(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
                    .clickable { action.invoke() }
            )
        }
    )
}

