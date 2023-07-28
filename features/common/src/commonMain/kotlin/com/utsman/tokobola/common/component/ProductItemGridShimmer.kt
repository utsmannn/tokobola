package com.utsman.tokobola.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ProductItemGridShimmer() {
    Column(
        modifier = Modifier.size(320.dp).padding(6.dp).shimmerBackground(shape = RectangleShape)
    ) {}
}