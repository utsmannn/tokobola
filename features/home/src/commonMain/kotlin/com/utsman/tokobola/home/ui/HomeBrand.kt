package com.utsman.tokobola.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.NativeView
import com.utsman.tokobola.common.component.shimmerBackground
import com.utsman.tokobola.common.entity.ui.Brand
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess

@Composable
fun HomeBrandView(brands: State<List<Brand>>) {
    NativeView {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            with(brands) {
                onLoading {
                    item {
                        HomeBrandShimmer()
                    }
                    item {
                        HomeBrandShimmer()
                    }
                    item {
                        HomeBrandShimmer()
                    }
                }
                onSuccess {
                    items(
                        items = it
                    ) {
                        HomeBrandItem(it) {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBrandItem(brand: Brand, clickAction: (Brand) -> Unit) {

    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(100.dp)
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