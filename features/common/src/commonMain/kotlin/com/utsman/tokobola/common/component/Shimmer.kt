package com.utsman.tokobola.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerH320() {
    Column(
        modifier = Modifier
            .height(320.dp)
            .fillMaxWidth()
            .padding(6.dp)
            .shimmerBackground(shape = RectangleShape)
    ) {}
}