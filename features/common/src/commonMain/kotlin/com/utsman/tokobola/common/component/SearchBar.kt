package com.utsman.tokobola.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun SearchBarStatic(modifier: Modifier = Modifier) {
    val statusBarHeight = rememberStatusBarHeightDp()

    val navigation = LocalNavigation.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.HeightTopBarSearch)
            .padding(
                top = 12.dp + statusBarHeight,
                bottom = 6.dp,
                start = 12.dp,
                end = 12.dp
            )
            .shadow(
                elevation = 128.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(18.dp))
                .clickable {
                    navigation.goToSearch()
                }
                .background(color = Color.White)
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                )
        ) {
            Text(
                "Search..",
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f),
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp
            )

            val painter = painterResource(SharedRes.images.icon_search)
            Image(
                painter = painter,
                modifier = Modifier.size(24.dp),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
            )
        }
    }
}

@Composable
fun SearchBarStaticWithTitle(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    backButton: @Composable () -> Unit = {},
    action: () -> Unit
) {
    val statusBarHeight = rememberStatusBarHeightDp()

    val navigation = LocalNavigation.current

    Column {
        Row {

        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(Dimens.HeightTopBarSearch)
                .padding(
                    top = 12.dp + statusBarHeight,
                    bottom = 6.dp,
                    start = 12.dp,
                    end = 12.dp
                )
                .shadow(
                    elevation = 128.dp,
                    shape = RoundedCornerShape(18.dp)
                ),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable {
                        navigation.goToSearch()
                    }
                    .background(color = Color.White)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
            ) {
                Text(
                    "Search..",
                    modifier = Modifier.align(Alignment.CenterVertically).weight(1f),
                    color = MaterialTheme.colors.primary,
                    fontSize = 14.sp
                )

                val painter = painterResource(SharedRes.images.icon_search)
                Image(
                    painter = painter,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
                )
            }
        }
    }
}