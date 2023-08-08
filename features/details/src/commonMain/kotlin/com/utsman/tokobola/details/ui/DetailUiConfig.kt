package com.utsman.tokobola.details.ui

import androidx.compose.ui.unit.IntSize

data class DetailUiConfig(
    var selectedImageIndex: Int = 0,
    var isShowImageDialog: Boolean = false,
    var globalSize: IntSize = IntSize.Zero
)