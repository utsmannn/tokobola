package com.utsman.tokobola.core.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
actual fun CoreAppBar(
    title: String,
    previousTitle: String?,
    action: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.clickable { action.invoke() }
                .weight(1f)
                .padding(all = 12.dp)
                .align(Alignment.CenterVertically)
        ) {
            val imageRes = SharedRes.images.arrow_back_ios
            val painter = painterResource(imageRes)
            Image(
                modifier = Modifier.size(24.dp),
                painter = painter,
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.Blue)
            )
            if (previousTitle != null) {
                AppText(
                    text = previousTitle,
                    fontSize = 14.sp,
                    color = Color.Blue,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        AppText(
            text = title,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
                .padding(all = 12.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}