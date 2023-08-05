package com.utsman.tokobola.common.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.asFont

@Composable
fun Type(): Typography {
    val regularRes = SharedRes.fonts.PTSans.regular
    val italicRes = SharedRes.fonts.PTSans.italic
    val boldRes = SharedRes.fonts.PTSans.bold

    val regularFont = regularRes.asFont()
    val italicFont = italicRes.asFont()
    val boldFont = boldRes.asFont()

    val defaultFontFamily = if (regularFont != null && italicFont != null && boldFont != null) {
        FontFamily(regularFont, italicFont, boldFont)
    } else {
        FontFamily.Default
    }

    return Typography(
        defaultFontFamily = defaultFontFamily
    )
}