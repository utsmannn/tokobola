package com.utsman.tokobola.details.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductTopBar
import com.utsman.tokobola.common.component.PullRefreshIndicatorOffset
import com.utsman.tokobola.common.component.ZoomableImage
import com.utsman.tokobola.common.entity.Product
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.currency
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.pxToDp
import com.utsman.tokobola.details.LocalDetailUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Detail(productId: Int) {

    val detailUseCase = LocalDetailUseCase.current
    val detailViewModel = rememberViewModel { DetailViewModel(detailUseCase) }

    val detailState by detailViewModel.detailState.collectAsState()

    val isLoading by derivedStateOf {
        detailState is State.Loading
    }

    val uiConfig by detailViewModel.uiConfig.collectAsState()


    val pullRefreshState = rememberPullRefreshState(refreshing = isLoading, onRefresh = {
        if (!uiConfig.isShowImageDialog) {
            detailViewModel.getDetail(productId)
        }
    })

    val navigation = LocalNavigation.current

    Scaffold(
        modifier = Modifier
            .onGloballyPositioned {
                detailViewModel.updateUiConfig {
                    uiConfig.copy(globalSize = it.size)
                }
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)
        ) {

            LazyColumn {
                with(detailState) {
                    onIdle {
                        detailViewModel.getDetail(productId)
                    }
                    onLoading {
                        item {
                            DetailLoading()
                        }
                    }
                    onSuccess { detail ->
                        item {
                            DetailSuccess(detail, detailViewModel)
                        }
                    }
                    onFailure { throwable ->
                        item {
                            ErrorScreen(throwable)
                        }
                    }
                }
            }

            ProductTopBar(
                modifier = Modifier, hideTitle = true, transparentBackground = true
            ) {
                if (uiConfig.isShowImageDialog) {
                    detailViewModel.updateUiConfig {
                        uiConfig.copy(isShowImageDialog = false)
                    }
                } else {
                    navigation.back()
                }
            }

            if (!uiConfig.isShowImageDialog) {
                PullRefreshIndicatorOffset(
                    refreshing = isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}


@Composable
fun DetailLoading() {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailSuccess(product: Product, viewModel: DetailViewModel) {

    LaunchedEffect(Unit) {
        viewModel.postProductViewed(product.id)
    }

    val uiConfig by viewModel.uiConfig.collectAsState()

    // need to notify image
    var isImageNeedDisplaying by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    Box {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth().height(340.dp)
            ) {
                val brandPainter = rememberImagePainter(product.brand.logo)

                this@Column.AnimatedVisibility(
                    visible = isImageNeedDisplaying,
                    enter = fadeIn(animationSpec = tween(100)),
                    exit = fadeOut(animationSpec = tween(100))
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize().zIndex(1f).clickable {
                            viewModel.updateUiConfig {
                                uiConfig.copy(isShowImageDialog = true)
                            }
                        },
                        painter = rememberImagePainter(product.images[uiConfig.selectedImageIndex]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

                Image(
                    painter = brandPainter,
                    contentDescription = null,
                    modifier = Modifier.size(54.dp).padding(12.dp).zIndex(2f)
                        .align(Alignment.BottomStart),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
                )

                Column(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp).zIndex(2f)
                        .background(
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                ) {
                    product.images.forEachIndexed { index, s ->
                        val eachPainter = rememberImagePainter(product.images[index])
                        val colorFilterAlpha by derivedStateOf {
                            if (index == uiConfig.selectedImageIndex) {
                                0.4f
                            } else {
                                0.0f
                            }
                        }

                        Image(
                            modifier = Modifier.size(60.dp).padding(6.dp)
                                .clip(RoundedCornerShape(8.dp)).shadow(32.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        isImageNeedDisplaying = false
                                        viewModel.updateUiConfig {
                                            uiConfig.copy(selectedImageIndex = index)
                                        }
                                        delay(500)
                                        isImageNeedDisplaying = true
                                    }
                                },
                            painter = eachPainter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(color = Color.Black.copy(alpha = colorFilterAlpha), blendMode = BlendMode.SrcAtop)
                        )
                    }
                }
            }

            val colorDot = MaterialTheme.colors.secondary

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.category, style = MaterialTheme.typography.subtitle2
                    )
                    Canvas(
                        modifier = Modifier.padding(6.dp).size(6.dp)
                    ) {
                        drawCircle(color = colorDot)
                    }

                    Text(
                        text = product.brand.name, style = MaterialTheme.typography.subtitle2
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth().padding(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = product.price.currency(),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.End
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth().padding(6.dp))
                Text(modifier = Modifier.fillMaxWidth(), text = product.description)
            }
        }

        AnimatedVisibility(
            visible = uiConfig.isShowImageDialog,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200)),
            modifier = Modifier.height(uiConfig.globalSize.height.pxToDp())
                .width(uiConfig.globalSize.width.pxToDp())
                .background(color = Color.Black.copy(alpha = 0.8f))
        ) {

            ZoomableImage(
                painter = rememberImagePainter(product.images[uiConfig.selectedImageIndex]),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            )
        }
    }


}