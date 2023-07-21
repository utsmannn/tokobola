package com.utsman.tokobola.core

fun Int?.orNol(): Int = this ?: 0
fun Double?.orNol(): Double = this ?: 0.0
fun Boolean?.orFalse(): Boolean = this ?: false