package com.utsman.tokobola.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.core.utils.pxToDp
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.database.localRepository
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

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
    lazyGridState: LazyGridState,
    title: String = "Anu mas"
) {

    val searchBarColor by lazyGridState.animatedTopBarColor

    val isTitleVisible by derivedStateOf {
        !lazyGridState.canScrollBackward
    }

    val statusBarHeight = rememberStatusBarHeightDp()
    val navigation = LocalNavigation.current

    val viewModel = rememberViewModel { SearchBarViewModel.instance() }
    val cartCount by viewModel.cartCount.collectAsState()

    Column(
        modifier = Modifier
            .background(color = searchBarColor)
            .padding(
                top = 12.dp + statusBarHeight,
                bottom = 6.dp,
                start = 12.dp,
                end = 12.dp
            )
    ) {

        AnimatedVisibility(
            visible = isTitleVisible
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        Box {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 12.dp)
                    .shadow(
                        elevation = 128.dp,
                        shape = RoundedCornerShape(18.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    modifier = Modifier.weight(1f)
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

                val cartColorFilter = if (isTitleVisible) {
                    ColorFilter.tint(color = MaterialTheme.colors.primary)
                } else {
                    ColorFilter.tint(color = Color.White)
                }

                Image(
                    painter = painterResource(SharedRes.images.icon_cart),
                    contentDescription = null,
                    colorFilter = cartColorFilter,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .clickable {
                            navigation.goToCart()
                        }
                )
            }

            DefaultAnimatedVisibility(
                isVisible = cartCount > 0,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-4).dp)
            ) {
                Box(modifier = Modifier
                        .size(18.dp)
                        .background(color = Color.Red, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$cartCount",
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

class SearchBarViewModel : ViewModel() {
    private val localRepository by localRepository()

    val cartCount = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            localRepository.selectAllCart()
                .collect {
                    cartCount.value = it.count()
                }
        }
    }

    companion object : SingletonCreator<SearchBarViewModel>() {
        fun instance() = create { SearchBarViewModel() }
    }
}