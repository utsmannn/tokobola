package com.utsman.tokobola.cart.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.cart.LocalCartUseCase
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ScaffoldGridState
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.TopBar
import com.utsman.tokobola.common.component.addShadow
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.currency
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess

@Composable
fun Cart() {
    val useCase = LocalCartUseCase.current
    val viewModel = rememberViewModel { CartViewModel(useCase) }

    val cartState by viewModel.cartState.collectAsState()
    val uiConfig by viewModel.cartUiConfig.collectAsState()

    val lazyGridState = rememberLazyGridState()

    val navigation = LocalNavigation.current

    LaunchedEffect(Unit) {
        viewModel.listenCart()
    }

    ScaffoldGridState(
        topBar = {
            TopBar(
                text = "Shopping Cart",
                lazyGridState = lazyGridState
            )
        },
        topBarPadding = Dimens.HeightTopBarSearch,
        lazyGridState = lazyGridState,
        modifier = Modifier.fillMaxSize(),
        fixColumn = 1,
    ) {

        items(uiConfig.carts.filter { it.quantity > 0 }) { cart ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {

                    Box(
                        modifier = Modifier.wrapContentSize()
                            .addShadow(12.dp)

                    ) {
                        val painter = rememberImagePainter(cart.product.image)
                        Image(
                            modifier = Modifier.size(70.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(Dimens.CornerSize)
                                )
                                .clip(RoundedCornerShape(Dimens.CornerSize))
                                .clickable {
                                    navigation.goToDetailProduct(cart.product.id)
                                },
                            painter = painter,
                            contentDescription = cart.product.name,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(all = 6.dp)
                            .weight(1f)
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = cart.product.brand.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp
                        )
                        Text(
                            text = cart.product.name,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = cart.product.price.currency(),
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colors.primary
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(34.dp).clip(CircleShape)
                                .background(color = MaterialTheme.colors.primary)
                                .clickable {
                                    viewModel.decrementCart(cart.product.id)
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
                                    viewModel.incrementCart(cart.product.id)
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

                Divider(modifier = Modifier.fillMaxWidth().padding(6.dp))
            }
        }

        with(cartState) {
            onLoading {
                item {
                    Shimmer()
                }
            }
            onSuccess { carts ->
                viewModel.pushCart(carts)
            }
            onFailure {
                item {
                    ErrorScreen(it)
                }
            }
        }
    }
}