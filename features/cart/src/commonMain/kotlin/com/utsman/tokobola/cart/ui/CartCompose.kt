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
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.cart.LocalCartUseCase
import com.utsman.tokobola.common.component.DefaultAnimatedVisibility
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.EmptyScreen
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ScaffoldGridState
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.TopBar
import com.utsman.tokobola.common.component.addShadow
import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.currency
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onLoadingComposed
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.onSuccessComposed
import com.utsman.tokobola.core.utils.rememberNavigationBarHeightDp
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun Cart() {
    val useCase = LocalCartUseCase.current
    val viewModel = rememberViewModel { CartViewModel(useCase) }

    val cartState by viewModel.cartState.collectAsState()
    val uiConfig by viewModel.cartUiConfig.collectAsState()

    val carts by derivedStateOf {
        uiConfig.carts.filter { it.quantity > 0 }
    }

    val lazyGridState = rememberLazyGridState()

    val navigation = LocalNavigation.current

    val placeState by viewModel.shippingLocationState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.listenCart()
        viewModel.getShippingLocation()
    }

    val navigationBarHeight = rememberNavigationBarHeightDp()

    Box {
        ScaffoldGridState(
            topBar = {
                TopBar(
                    text = "Shopping Cart",
                    lazyGridState = lazyGridState
                )
            },
            topBarPadding = 110.dp,
            lazyGridState = lazyGridState,
            modifier = Modifier.fillMaxSize(),
            bottomBarPadding = 220.dp,
            fixColumn = 1,
        ) {

            if (carts.isEmpty()) {
                item {
                    EmptyScreen()
                }
            } else {
                items(carts) { cart ->
                    ItemCart(
                        cart = cart,
                        increment = {
                            viewModel.incrementCart(cart.product.id)
                        },
                        decrement = {
                            viewModel.decrementCart(cart.product.id)
                        },
                        toDetail = {
                            navigation.goToDetailProduct(cart.product.id)
                        }
                    )
                }
            }


            with(cartState) {
                onLoading {
                    item {
                        Shimmer()
                    }
                }
                onSuccess { carts ->
                    if (carts.isEmpty()) {
                        item {
                            EmptyScreen()
                        }
                    } else {
                        viewModel.pushCart(carts)
                    }
                }
                onFailure {
                    item {
                        ErrorScreen(it)
                    }
                }
            }
        }

        DefaultAnimatedVisibility(
            isVisible = !uiConfig.isCartEmpty(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val shippingPainter = painterResource(SharedRes.images.icon_shipping)

            Column(
                modifier = Modifier
                    .shadow(14.dp)
                    .background(color = Color.White)
            ) {
                with(placeState) {
                    onLoadingComposed {
                        Shimmer(modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 12.dp).padding(top = 12.dp))
                    }
                    onSuccessComposed { place ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navigation.goToLocationPicker()
                                }
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Shipping address :",
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = place.name
                                )
                            }

                            Image(
                                painter = shippingPainter,
                                modifier = Modifier.padding(start = 12.dp).size(25.dp),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp + navigationBarHeight)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = uiConfig.amount().currency(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp
                    )

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp, top = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = MaterialTheme.colors.primary)
                            .clickable {

                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Checkout",
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
        }
    }
}

@Composable
fun ItemCart(
    cart: Cart,
    increment: () -> Unit,
    decrement: () -> Unit,
    toDetail: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
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
                            toDetail.invoke()
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
                        .size(24.dp).clip(CircleShape)
                        .background(color = MaterialTheme.colors.primary)
                        .clickable {
                            decrement.invoke()
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
                            top = 6.dp,
                            bottom = 6.dp
                        ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(24.dp).clip(CircleShape)
                        .background(color = MaterialTheme.colors.primary)
                        .clickable {
                            increment.invoke()
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