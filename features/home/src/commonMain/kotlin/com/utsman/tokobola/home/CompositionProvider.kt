package com.utsman.tokobola.home

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.home.domain.HomeDataSources
import com.utsman.tokobola.home.domain.HomeRepository
import com.utsman.tokobola.home.domain.HomeUseCase
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object HomeInstanceProvider : SynchronizObject() {

    private var dataSources: HomeDataSources? = null
    private var repository: HomeRepository? = null
    private var useCase: HomeUseCase? = null

    private fun getDataSources(): HomeDataSources {
        if (dataSources == null) dataSources = HomeDataSources()

        return synchroniz(this) { dataSources!! }
    }

    private fun getRepository(): HomeRepository {
        if (repository == null) repository = HomeRepository(getDataSources())

        return synchroniz(this) { repository!! }
    }

    fun providedUseCase(): HomeUseCase {
        if (useCase == null) useCase = HomeUseCase(getRepository())

        return synchroniz(this) { useCase!! }
    }
}

val LocalHomeUseCase = compositionLocalOf<HomeUseCase> { error("Not Provided") }