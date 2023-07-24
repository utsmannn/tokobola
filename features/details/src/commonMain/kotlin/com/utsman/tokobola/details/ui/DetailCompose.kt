package com.utsman.tokobola.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.composable.AppText
import com.utsman.tokobola.core.composable.CoreAppBar
import com.utsman.tokobola.core.onFailure
import com.utsman.tokobola.core.onIdle
import com.utsman.tokobola.core.onLoading
import com.utsman.tokobola.core.onSuccess
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.details.LocalDetailUseCase
import com.utsman.tokobola.details.entity.ProductDetail

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Detail(productId: Int) {

    val detailUseCase = LocalDetailUseCase.current
    val detailViewModel = rememberViewModel { DetailViewModel(detailUseCase) }

    val detailState by detailViewModel.detailState.collectAsState()

    val isLoading by derivedStateOf {
        detailState is State.Loading
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            detailViewModel.getDetail(productId)
        }
    )

    var productName by remember { mutableStateOf("") }

    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            CoreAppBar(title = productName, previousTitle = "Home") {
                navigator.pop()
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                with(detailState) {
                    onIdle {
                        detailViewModel.getDetail(productId)
                    }
                    onLoading {
                        DetailLoading()
                    }
                    onSuccess { detail ->
                        productName = detail.name
                        DetailSuccess(detail) {
                            navigator.pop()
                        }
                    }
                    onFailure { throwable ->
                        DetailFailure(throwable.message.orEmpty())
                    }
                }
            }
            PullRefreshIndicator(
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
fun DetailSuccess(productDetail: ProductDetail, action: () -> Unit) {
    Column {
        val painter = rememberImagePainter(productDetail.image[0])
        Image(
            modifier = Modifier.fillMaxWidth().size(200.dp),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )

        Divider()
        AppText(
            modifier = Modifier.fillMaxWidth().clickable { action.invoke() },
            text = productDetail.name
        )
        AppText(modifier = Modifier.fillMaxWidth(), text = productDetail.category)
        Divider()
        AppText(modifier = Modifier.fillMaxWidth(), text = productDetail.description)
    }
}

@Composable
fun DetailFailure(message: String) {
    AppText(text = message)
}