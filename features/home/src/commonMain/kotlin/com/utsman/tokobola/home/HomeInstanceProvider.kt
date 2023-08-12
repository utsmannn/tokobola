package com.utsman.tokobola.home

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.home.domain.HomeRepository
import com.utsman.tokobola.home.domain.HomeUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

object HomeInstanceProvider {

    fun providedUseCase(): HomeUseCase {
        val repo = HomeRepository.create { HomeRepository() }
        return HomeUseCase.create { HomeUseCase(repo) }
    }
}

val LocalHomeUseCase = compositionLocalOf<HomeUseCase> { error("Not Provided") }