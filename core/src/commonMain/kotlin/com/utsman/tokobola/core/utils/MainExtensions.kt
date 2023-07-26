package com.utsman.tokobola.core.utils

import androidx.compose.ui.graphics.Color
import com.utsman.tokobola.resources.MokoColor
import dev.icerock.moko.graphics.parseColor

fun Double.currency(): String {
    return "$$this"
}

fun Color.Companion.parseString(hex: String): Color {
    return Color(MokoColor.parseColor(hex).argb)
}