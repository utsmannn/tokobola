package com.utsman.tokobola.details.ui.product

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.DefaultAnimatedVisibility
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductTopBar
import com.utsman.tokobola.common.component.PullRefreshIndicatorOffset
import com.utsman.tokobola.common.component.ZoomableImage
import com.utsman.tokobola.common.entity.Product
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.currency
import com.utsman.tokobola.core.utils.onFailureComposed
import com.utsman.tokobola.core.utils.onLoadingComposed
import com.utsman.tokobola.core.utils.onSuccessComposed
import com.utsman.tokobola.core.utils.pxToDp
import com.utsman.tokobola.core.utils.rememberNavigationBarHeightDp
import com.utsman.tokobola.details.LocalProductDetailUseCase
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductDetail(productId: Int) {

    val detailUseCase = LocalProductDetailUseCase.current
    val viewModel = rememberViewModel { ProductDetailViewModel(detailUseCase) }

    val detailState by viewModel.detailState.collectAsState()

    val isLoading by derivedStateOf {
        detailState is State.Loading
    }

    val uiConfig by viewModel.uiConfig.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing = isLoading, onRefresh = {
        viewModel.getDetail(productId)
        viewModel.getCart(productId)
    })

    val navigation = LocalNavigation.current

    LaunchedEffect(Unit) {
        viewModel.getDetail(productId)
        viewModel.getCart(productId)
        viewModel.listenWishlist(productId)
    }

    Scaffold(
        modifier = Modifier
            .onGloballyPositioned {
                viewModel.updateUiConfig {
                    uiConfig.copy(globalSize = it.size)
                }
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
        ) {

            with(detailState) {
                onLoadingComposed {
                    DetailLoading()
                }
                onSuccessComposed { detail ->
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                            .height(uiConfig.globalSize.height.pxToDp())
                    ) {
                        DetailSuccess(detail, viewModel)
                    }

                }
                onFailureComposed { throwable ->
                    ErrorScreen(throwable)
                }
            }

            ProductTopBar(
                modifier = Modifier, hideTitle = true, transparentBackground = true
            ) {
                if (uiConfig.isShowImageDialog) {
                    viewModel.updateUiConfig {
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
fun DetailSuccess(product: Product, viewModel: ProductDetailViewModel) {

    val cart by viewModel.productCart.collectAsState()
    val isExistInWishlist by viewModel.wishlistState.collectAsState()

    val isCartEmpty by derivedStateOf {
        cart.isEmpty()
    }

    val resourceWishlist by derivedStateOf {
        if (isExistInWishlist) {
            SharedRes.images.icon_bookmark_fill
        } else {
            SharedRes.images.icon_bookmark_outline
        }
    }

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

                DefaultAnimatedVisibility(
                    isVisible = isImageNeedDisplaying,
                    duration = 100
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize().clickable {
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
                    modifier = Modifier.size(54.dp).padding(12.dp)
                        .align(Alignment.BottomStart),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
                )

                Column(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp)
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
//                            colorFilter = ColorFilter.tint(color = Color.Black.copy(alpha = colorFilterAlpha), blendMode = BlendMode.SrcAtop) // blend mode not work in ios
                        )
                    }
                }
            }

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
                        drawCircle(color = Color.Black)
                    }

                    Text(
                        text = product.brand.name, style = MaterialTheme.typography.subtitle2
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(resourceWishlist),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                            .clickable {
                                viewModel.toggleWishlist(product.id)
                            },
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
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

        DefaultAnimatedVisibility(
            isVisible = isCartEmpty,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ButtonEmptyCart(product, viewModel)
        }

        DefaultAnimatedVisibility(
            isVisible = !isCartEmpty,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ButtonFoundCart(product, viewModel)
        }

        DefaultAnimatedVisibility(
            isVisible = uiConfig.isShowImageDialog,
            modifier = Modifier.fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.8f))
        ) {
            ZoomableImage(
                painter = rememberImagePainter(product.images[uiConfig.selectedImageIndex]),
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun ButtonEmptyCart(product: Product, viewModel: ProductDetailViewModel) {
    val navigationBarHeight = rememberNavigationBarHeightDp()

    Column(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .shadow(elevation = 12.dp)
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    bottom = 12.dp + navigationBarHeight,
                    end = 12.dp,
                    start = 12.dp
                )
                .clip(RoundedCornerShape(12.dp))
                .background(color = MaterialTheme.colors.primary)
                .clickable {
                    viewModel.incrementCart(product.id)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Add to cart",
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Image(
                painter = painterResource(SharedRes.images.icon_cart),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }
}

@Composable
fun ButtonFoundCart(product: Product, viewModel: ProductDetailViewModel) {
    val navigationBarHeight = rememberNavigationBarHeightDp()
    val cart by viewModel.productCart.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .shadow(elevation = 12.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = 12.dp,
                    bottom = 12.dp + navigationBarHeight,
                    end = 12.dp,
                    start = 12.dp
                )
                .clip(RoundedCornerShape(12.dp))
                .background(color = MaterialTheme.colors.primary)
                .clickable {

                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Continue to cart",
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Image(
                painter = painterResource(SharedRes.images.icon_cart),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }

        Row(
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    bottom = 12.dp + navigationBarHeight,
                    end = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(34.dp).clip(CircleShape)
                    .background(color = MaterialTheme.colors.primary)
                    .clickable {
                        viewModel.decrementCart(product.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "${cart.quantity}",
                modifier = Modifier
                    .width(50.dp)
                    .padding(
                        end = 6.dp,
                        start = 6.dp
                    ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(34.dp).clip(CircleShape)
                    .background(color = MaterialTheme.colors.primary)
                    .clickable {
                        viewModel.incrementCart(product.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}