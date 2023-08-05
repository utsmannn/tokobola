package com.utsman.tokobola.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.shimmerBackground
import com.utsman.tokobola.common.entity.Brand

@Composable
fun HomeBrandItem(brand: Brand, clickAction: (Brand) -> Unit) {

    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(Dimens.CornerSize))
            .background(color = MaterialTheme.colors.secondary.copy(alpha = 0.3f))
            .clickable { clickAction.invoke(brand) }
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = rememberImagePainter(brand.logo)
        Image(
            painter = painter,
            contentDescription = brand.name,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = brand.name,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}

@Composable
fun HomeBrandShimmer() {

    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(100.dp)
            .shimmerBackground()
    ) {}
}