package com.utsman.tokobola.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun TopBar(text: String, modifier: Modifier = Modifier, lazyGridState: LazyGridState) {
    val navigation = LocalNavigation.current
    val titleColor by lazyGridState.animatedColor(
        from = Color.White,
        to = MaterialTheme.colors.primary
    )

    val topBarColor by lazyGridState.animatedTopBarColor

    Row(
        modifier = modifier
            .background(color = topBarColor)
            .wrapContentHeight()
            .padding(
                top = 12.dp + rememberStatusBarHeightDp(),
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(34.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(false),
                    onClick = {
                        navigation.back()
                    })
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colors.secondary.copy(alpha = 0.6f)
                )
                .padding(6.dp),
            painter = painterResource(SharedRes.images.arrow_back_default),
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.White)
        )

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f).padding(12.dp),
            color = titleColor
        )
    }
}