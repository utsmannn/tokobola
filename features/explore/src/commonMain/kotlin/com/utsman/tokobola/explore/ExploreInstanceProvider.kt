package com.utsman.tokobola.explore

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.explore.domain.ExploreRepository
import com.utsman.tokobola.explore.domain.ExploreUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ExploreInstanceProvider : SynchronizObject() {

    @Volatile
    private var repository: ExploreRepository? = null

    @Volatile
    private var useCase: ExploreUseCase? = null

    private fun getRepository(): ExploreRepository {
        if (repository == null) repository = ExploreRepository()
        return synchroniz(this) { repository!! }
    }

    fun providedUseCase(): ExploreUseCase {
        if (useCase == null) useCase = ExploreUseCase(getRepository())
        return synchroniz(this) { useCase!! }
    }
}

val LocalExploreUseCase = compositionLocalOf<ExploreUseCase> { error("Explore UseCase not provided") }