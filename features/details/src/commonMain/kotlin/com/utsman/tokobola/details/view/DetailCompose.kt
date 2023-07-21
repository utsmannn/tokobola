package com.utsman.tokobola.details.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.composable.AppText
import com.utsman.tokobola.core.onFailure
import com.utsman.tokobola.core.onIdle
import com.utsman.tokobola.core.onLoading
import com.utsman.tokobola.core.onSuccess
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.details.LocalDetailUseCase
import com.utsman.tokobola.details.entity.ProductDetail

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Detail(productId: String) {

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

    Scaffold {
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
                        DetailSuccess(detail)
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
fun DetailSuccess(productDetail: ProductDetail) {
    Column {
        val painter = rememberImagePainter(productDetail.image[0])
        Image(
            modifier = Modifier.fillMaxWidth().size(200.dp),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )

        Divider()
        AppText(modifier = Modifier.fillMaxWidth(), text = productDetail.name)
        AppText(modifier = Modifier.fillMaxWidth(), text = productDetail.category)
        Divider()
        AppText(modifier = Modifier.fillMaxWidth(), text = productDetail.description)
    }
}

@Composable
fun DetailFailure(message: String) {
    AppText(text = message)
}