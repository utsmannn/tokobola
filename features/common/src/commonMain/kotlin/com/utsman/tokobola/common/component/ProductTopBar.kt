package com.utsman.tokobola.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun ProductTopBar(
    modifier: Modifier = Modifier,
    title: String,
    hideTitle: Boolean = false,
    transparentBackground: Boolean = false,
    backAction: () -> Unit = {}
) {

    val statusBarHeight = PlatformUtils.rememberStatusBarHeight()

    Row(
        modifier = modifier
            .background(
                if (transparentBackground) Color.Transparent else MaterialTheme.colors
                    .primary
                    .copy(alpha = 0.6f)
            )
            .fillMaxWidth().padding(
                top = statusBarHeight.dp,
                bottom = 12.dp,
                start = 12.dp,
                end = 12.dp
            ).wrapContentHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val backResources = SharedRes.images.arrow_back_default
        val painter = painterResource(backResources)
        Image(
            modifier = Modifier.size(24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(false),
                    onClick = { backAction.invoke() }),
            painter = painter,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondaryVariant)
        )
        Spacer(modifier = Modifier.size(22.dp))
        if (!hideTitle) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

}