package com.utsman.tokobola.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.entity.ui.Product
import com.utsman.tokobola.core.utils.currency

@Composable
fun ProductItemGrid(product: Product, clickAction: (Product) -> Unit) {
    Column(
        modifier = Modifier.height(320.dp)
            .padding(6.dp)
            .background(color = MaterialTheme.colors.secondary.copy(alpha = 0.3f))
            .clickable { clickAction.invoke(product) }
    ) {
        val painter = rememberImagePainter(product.image.first())
        Image(
            modifier = Modifier.fillMaxWidth().aspectRatio(1.8f / 2f),
            painter = painter,
            contentDescription = product.name,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(all = 6.dp)
        ) {
            Text(
                text = product.brand,
                fontSize = 12.sp
            )
            Text(
                text = product.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = product.category,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = product.price.currency(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}