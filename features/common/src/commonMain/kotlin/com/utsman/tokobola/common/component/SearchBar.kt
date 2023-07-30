package com.utsman.tokobola.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun SearchBarStatic(modifier: Modifier, action: () -> Unit) {
    val statusBarHeight = PlatformUtils.rememberStatusBarHeight()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(top = statusBarHeight.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
                .background(color = Color.Black.copy(alpha = 0.1f))
                .clickable { action.invoke() }
                .padding(6.dp)
        ) {
            Text(
                "Search",
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )

            val painter = painterResource(SharedRes.images.icon_search)
            Image(
                painter = painter,
                modifier = Modifier.size(24.dp),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }
}