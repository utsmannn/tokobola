package com.utsman.tokobola.cart.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.tokobola.cart.LocalLocationPickerUseCase
import com.utsman.tokobola.common.component.DefaultAnimatedVisibility
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.MapConfigState
import com.utsman.tokobola.common.component.MapView
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.rememberMapConfigState
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.onLoadingComposed
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.onSuccessComposed
import com.utsman.tokobola.core.utils.rememberNavigationBarHeightDp
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun LocationPicker(latLon: LatLon) {
    val navigation = LocalNavigation.current
    val useCase = LocalLocationPickerUseCase.current
    val viewModel = rememberViewModel { LocationPickerViewModel(useCase) }


    val mapConfigState = rememberMapConfigState(latLon)

    val locationResultState by viewModel.locationResultState.collectAsState()
    val locationReverseState by viewModel.locationReverseState.collectAsState()

    val isReverseLoading by derivedStateOf {
        locationReverseState is State.Loading
    }

    val modifierMarker by derivedStateOf {
        if (isReverseLoading) {
            Modifier.fillMaxSize()
        } else {
            Modifier.wrapContentSize()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.listenQuery()
        viewModel.updateProximityLatLon(latLon)
        viewModel.getLocationReverse(latLon)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            )
        },
        drawerGesturesEnabled = true
    ) {
        Box {
            MapView(
                modifier = Modifier.fillMaxSize(),
                mapConfigState = mapConfigState
            )

            Box(
                modifier = modifierMarker
                    .align(Alignment.Center),
            ) {
                val painterMarker = painterResource(SharedRes.images.icon_pin_location)
                Image(
                    painter = painterMarker,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                        .offset(y = (-56).dp)
                        .align(Alignment.Center),
                    colorFilter = ColorFilter.tint(color = Color.Red)
                )
            }

            with(locationResultState) {
                onLoadingComposed {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .shadow(12.dp)
                            .background(color = Color.White)
                            .height(120.dp)
                    ) {
                        Shimmer(
                            modifier = Modifier.fillMaxSize()
                                .padding(12.dp)
                        )
                    }
                }
                onSuccessComposed { places ->
                    DefaultAnimatedVisibility(
                        isVisible = places.isNotEmpty()
                    ) {
                        LazyColumn(
                            Modifier.fillMaxWidth()
                                .shadow(12.dp)
                                .background(color = Color.White),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(places) {
                                Text(
                                    text = it.name,
                                    modifier = Modifier.fillMaxWidth()
                                        .clickable {
                                            viewModel.clearData()
                                            viewModel.updateProximityLatLon(it.latLon)
                                            viewModel.getLocationReverse(it.latLon)
                                            mapConfigState.setLocation(it.latLon)
                                        }
                                        .padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.wrapContentHeight()
                    .align(Alignment.BottomCenter)

            ) {
                ZoomControl(
                    modifier = Modifier.align(Alignment.End).padding(12.dp),
                    state = mapConfigState
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .shadow(14.dp)
                        .background(color = Color.White)
                        .padding(
                            top = 12.dp,
                            bottom = 12.dp + rememberNavigationBarHeightDp(),
                            start = 12.dp,
                            end = 12.dp
                        )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .wrapContentHeight()
                                .weight(1f)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            with(locationReverseState) {
                                onLoadingComposed {
                                    Shimmer(
                                        modifier = Modifier.height(40.dp).fillMaxWidth()
                                    )
                                }
                                onSuccessComposed { place ->
                                    Text(
                                        text = place.name
                                    )
                                }
                            }
                        }

                        val painterMarker = painterResource(SharedRes.images.icon_plant)
                        Image(
                            painter = painterMarker,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp)
                                .padding(12.dp)
                                .clickable {
                                    viewModel.getLocationReverse(mapConfigState.getCenterLocation())
                                },
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = MaterialTheme.colors.primary)
                            .clickable {
                                navigation.back()
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Use this address",
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
fun SearchBar(modifier: Modifier = Modifier, viewModel: LocationPickerViewModel) {
    val statusBarHeight = rememberStatusBarHeightDp()

    val query by viewModel.query.collectAsState()
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
        val backResources = SharedRes.images.arrow_back_default
        val painter = painterResource(backResources)
        Image(
            modifier = Modifier.size(34.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(false),
                    onClick = {
                        navigation.back()
                    })
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colors.secondary.copy(alpha = 0.6f)
                )
                .padding(6.dp),
            painter = painter,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondaryVariant)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(18.dp))
                .background(color = Color.White)
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                )
        ) {

            BasicTextField(
                value = query,
                onValueChange = {
                    viewModel.query.value = it
                },
                textStyle = TextStyle.Default.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary
                ),
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
                decorationBox = { innerField ->
                    if (query.isEmpty()) {
                        Text(
                            "Search area or places",
                            modifier = Modifier.alpha(0.4f),
                            fontSize = 14.sp
                        )
                    }
                    innerField.invoke()
                }
            )
        }
    }
}

@Composable
fun ZoomControl(modifier: Modifier, state: MapConfigState) {

    Row(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .size(34.dp).clip(CircleShape)
                .background(color = MaterialTheme.colors.primary)
                .clickable {
                    state.zoomIn()
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

        Box(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(34.dp).clip(CircleShape)
                .background(color = MaterialTheme.colors.primary)
                .clickable {
                    state.zoomOut()
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