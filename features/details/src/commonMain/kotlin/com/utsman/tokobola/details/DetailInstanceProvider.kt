package com.utsman.tokobola.details

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.details.domain.DetailDataSources
import com.utsman.tokobola.details.domain.DetailRepository
import com.utsman.tokobola.details.domain.DetailUseCase
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DetailInstanceProvider : SynchronizObject() {

    private var dataSources: DetailDataSources? = null
    private var repository: DetailRepository? = null
    private var useCase: DetailUseCase? = null

    private fun getDataSource(): DetailDataSources {
        if (dataSources == null) dataSources = DetailDataSources()
        return synchroniz(this) { dataSources!! }
    }

    private fun getRepository(): DetailRepository {
        if (repository == null) repository = DetailRepository(getDataSource())
        return synchroniz(this) { repository!! }
    }

    fun providedUseCase(): DetailUseCase {
        if (useCase == null) useCase = DetailUseCase(getRepository())
        return synchroniz(this) { useCase!! }
    }
}

val LocalDetailUseCase = compositionLocalOf<DetailUseCase> { error("Not provided") }