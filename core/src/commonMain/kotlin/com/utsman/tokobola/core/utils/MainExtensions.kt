package com.utsman.tokobola.core.utils

import androidx.compose.ui.graphics.Color
import com.utsman.tokobola.resources.MokoColor
import dev.icerock.moko.graphics.parseColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun Double.currency(): String {
    return "$$this"
}

fun Color.Companion.parseString(hex: String): Color {
    return Color(MokoColor.parseColor(hex).argb)
}

// object : CoroutineScope {
//        override val coroutineContext: CoroutineContext
//            get() = SupervisorJob() + Dispatchers.IO
//    }

fun IoScope(): CoroutineScope = object : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO
}

// suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
//    map { async { f(it) } }.awaitAll()
//}

suspend fun <T, U> Iterable<T>.pmap(mapper: suspend (T) -> U): List<U> = withContext(Dispatchers.IO) {
    map { async { mapper.invoke(it) } }.awaitAll()
}