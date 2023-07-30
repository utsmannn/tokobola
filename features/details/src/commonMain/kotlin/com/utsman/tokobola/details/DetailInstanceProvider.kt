package com.utsman.tokobola.details

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.details.domain.DetailRepository
import com.utsman.tokobola.details.domain.DetailUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DetailInstanceProvider : SynchronizObject() {

    @Volatile
    private var repository: DetailRepository? = null
    @Volatile
    private var useCase: DetailUseCase? = null

    private fun getRepository(): DetailRepository {
        if (repository == null) repository = DetailRepository()
        return synchroniz(this) { repository!! }
    }

    fun providedUseCase(): DetailUseCase {
        if (useCase == null) useCase = DetailUseCase(getRepository())
        return synchroniz(this) { useCase!! }
    }
}

val LocalDetailUseCase = compositionLocalOf<DetailUseCase> { error("Not provided") }