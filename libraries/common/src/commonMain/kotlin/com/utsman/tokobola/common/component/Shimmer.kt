package com.utsman.tokobola.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Shimmer(modifier: Modifier = Modifier.height(320.dp)) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .shimmerBackground(shape = RoundedCornerShape(Dimens.CornerSize))
    )
}