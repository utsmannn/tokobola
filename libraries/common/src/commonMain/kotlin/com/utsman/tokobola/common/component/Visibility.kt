package com.utsman.tokobola.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultAnimatedVisibility(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    duration: Int = 200,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(duration)),
        exit = fadeOut(animationSpec = tween(duration)),
        modifier = modifier,
        content = content
    )
}