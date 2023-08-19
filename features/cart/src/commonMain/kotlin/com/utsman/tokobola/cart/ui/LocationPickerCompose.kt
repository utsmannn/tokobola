package com.utsman.tokobola.cart.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.tokobola.cart.LocalLocationPickerUseCase
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.MapConfigState
import com.utsman.tokobola.common.component.MapView
import com.utsman.tokobola.common.component.rememberMapConfigState
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.onLoadingComposed
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.rememberNavigationBarHeightDp
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun LocationPicker() {
    val useCase = LocalLocationPickerUseCase.current
    val viewModel = rememberViewModel { LocationPickerViewModel(useCase) }

    val mapConfigState = rememberMapConfigState()

    val locationState by viewModel.locationState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getLocation()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            )
        }
    ) {
        Box {
            MapView(
                modifier = Modifier.fillMaxSize(),
                mapConfigState = mapConfigState
            )


            with(locationState) {
                onLoadingComposed {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(12.dp)
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                    )
                }
                onSuccess {
                    mapConfigState.setLocation(it)
                }
            }

            ZoomControl(
                modifier = Modifier.align(Alignment.BottomEnd),
                state = mapConfigState
            )
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
                .padding(6.dp)
            ,
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
                textStyle = TextStyle.Default.copy(fontSize = 14.sp, color = MaterialTheme.colors.primary),
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
                decorationBox = { innerField ->
                    if (query.isEmpty()) {
                        Text("Search area or places", modifier = Modifier.alpha(0.4f), fontSize = 14.sp)
                    }
                    innerField.invoke()
                }
            )
        }
    }
}

@Composable
fun ZoomControl(modifier: Modifier, state: MapConfigState) {
    val navigationBarHeight = rememberNavigationBarHeightDp()

    Row(
        modifier = modifier
            .padding(
                top = 12.dp,
                bottom = 12.dp + navigationBarHeight,
                end = 12.dp
            )

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