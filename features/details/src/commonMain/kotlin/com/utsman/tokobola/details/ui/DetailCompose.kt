package com.utsman.tokobola.details.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.ProductPullRefreshIndicator
import com.utsman.tokobola.common.component.ProductTopBar
import com.utsman.tokobola.common.entity.ui.Product
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.utils.onFailureComposed
import com.utsman.tokobola.core.utils.onIdleComposed
import com.utsman.tokobola.core.utils.onLoadingComposed
import com.utsman.tokobola.core.utils.onSuccessComposed
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.currency
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.details.LocalDetailUseCase

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Detail(productId: Int) {

    val detailUseCase = LocalDetailUseCase.current
    val detailViewModel = rememberViewModel { DetailViewModel(detailUseCase) }

    val detailState by detailViewModel.detailState.collectAsState()

    val isLoading by derivedStateOf {
        detailState is State.Loading
    }

    val pullRefreshState = rememberPullRefreshState(refreshing = isLoading, onRefresh = {
        detailViewModel.getDetail(productId)
    })

    val navigation = LocalNavigation.current

    Scaffold {
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
                            DetailSuccess(detail)
                        }
                    }
                    onFailure { throwable ->
                        item {
                            DetailFailure(throwable.message.orEmpty())
                        }
                    }
                }
            }

            ProductTopBar(
                modifier = Modifier,
                hideTitle = true,
                transparentBackground = true
            ) {
                navigation.back()
            }

            ProductPullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@Composable
fun DetailLoading() {

}

@Composable
fun DetailSuccess(product: Product) {
    Column {
        val painter = rememberImagePainter(product.images[0])
        Image(
            modifier = Modifier.fillMaxWidth().size(340.dp),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        val colorDot = MaterialTheme.colors.secondary

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.subtitle2
                )
                Canvas(
                    modifier = Modifier.padding(6.dp).size(6.dp)
                ) {
                    drawCircle(color = colorDot)
                }
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.subtitle2
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
}

@Composable
fun DetailFailure(message: String) {
    Text(text = message)
}