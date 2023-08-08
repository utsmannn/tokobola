package com.utsman.tokobola.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.utils.currency

@Composable
fun ProductItemGrid(thumbProduct: ThumbnailProduct, clickAction: (ThumbnailProduct) -> Unit = {}) {
    val navigation = LocalNavigation.current


    Column(
        modifier = Modifier.height(Dimens.HeightProductItemGrid)
            .padding(6.dp)
    ) {

        Box(
            modifier = Modifier.wrapContentSize()
                .addShadow()

        ) {
            val painter = rememberImagePainter(thumbProduct.image)
            Image(
                modifier = Modifier.fillMaxWidth().aspectRatio(2f / 1.6f)
                    .background(color = Color.White, shape = RoundedCornerShape(Dimens.CornerSize))
                    .clip(RoundedCornerShape(Dimens.CornerSize))
                    .clickable {
                        clickAction.invoke(thumbProduct)
                        navigation.goToDetail(thumbProduct.id)
                    },
                painter = painter,
                contentDescription = thumbProduct.name,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .padding(all = 6.dp)
        ) {
            Text(
                text = thumbProduct.brand.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
            Text(
                text = thumbProduct.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = thumbProduct.price.currency(),
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = MaterialTheme.colors.primary
            )

            Divider(modifier = Modifier.fillMaxWidth().padding(6.dp))
        }
    }
}


@Composable
fun ProductItemGridRectangle(
    thumbProduct: ThumbnailProduct,
    clickAction: (ThumbnailProduct) -> Unit = {}
) {
    val navigation = LocalNavigation.current

    Column(
        modifier = Modifier
            .width(100.dp)
            .height(Dimens.HeightProductItemGridRectangle)
            .padding(6.dp)
    ) {

        Box(
            modifier = Modifier.wrapContentSize()
                .addShadow(12.dp)

        ) {
            val painter = rememberImagePainter(thumbProduct.image)
            Image(
                modifier = Modifier.fillMaxWidth().aspectRatio(2f / 1.6f)
                    .background(color = Color.White, shape = RoundedCornerShape(Dimens.CornerSize))
                    .clip(RoundedCornerShape(Dimens.CornerSize))
                    .clickable {
                        clickAction.invoke(thumbProduct)
                        navigation.goToDetail(thumbProduct.id)
                    },
                painter = painter,
                contentDescription = thumbProduct.name,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .padding(all = 6.dp)
        ) {
            Text(
                text = thumbProduct.brand.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 8.sp
            )
            Text(
                text = thumbProduct.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = thumbProduct.price.currency(),
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary
            )
        }
    }
}