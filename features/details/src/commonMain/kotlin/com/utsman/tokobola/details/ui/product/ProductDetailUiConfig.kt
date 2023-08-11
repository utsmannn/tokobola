package com.utsman.tokobola.details.ui.product

import androidx.compose.ui.unit.IntSize

data class ProductDetailUiConfig(
    var selectedImageIndex: Int = 0,
    var isShowImageDialog: Boolean = false,
    var globalSize: IntSize = IntSize.Zero
)