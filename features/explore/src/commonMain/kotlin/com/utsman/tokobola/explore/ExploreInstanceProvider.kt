package com.utsman.tokobola.explore

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.explore.domain.ExploreRepository
import com.utsman.tokobola.explore.domain.explore.ExploreUseCase
import com.utsman.tokobola.explore.domain.search.SearchUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ExploreInstanceProvider : SynchronizObject() {

    @Volatile
    private var repository: ExploreRepository? = null

    @Volatile
    private var exploreUseCase: ExploreUseCase? = null

    @Volatile
    private var searchUseCase: SearchUseCase? = null

    private fun getRepository(): ExploreRepository {
        if (repository == null) repository = ExploreRepository()
        return synchroniz(this) { repository!! }
    }

    fun providedExploreUseCase(): ExploreUseCase {
        if (exploreUseCase == null) exploreUseCase = ExploreUseCase(getRepository())
        return synchroniz(this) { exploreUseCase!! }
    }

    fun providedSearchUseCase(): SearchUseCase {
        if (searchUseCase == null) searchUseCase = SearchUseCase(getRepository())
        return synchroniz(this) { searchUseCase!! }
    }
}

val LocalExploreUseCase = compositionLocalOf<ExploreUseCase> { error("Explore UseCase not provided") }
val LocalSearchUseCase = compositionLocalOf<SearchUseCase> { error("Search UseCase not provided") }