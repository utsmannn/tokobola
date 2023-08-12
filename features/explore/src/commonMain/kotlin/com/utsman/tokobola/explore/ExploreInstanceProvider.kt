package com.utsman.tokobola.explore

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.explore.domain.ExploreRepository
import com.utsman.tokobola.explore.domain.explore.ExploreUseCase
import com.utsman.tokobola.explore.domain.search.SearchUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

object ExploreInstanceProvider {

    private fun getRepository(): ExploreRepository {
        return ExploreRepository.create { ExploreRepository() }
    }

    fun providedExploreUseCase(): ExploreUseCase {
        return ExploreUseCase.create { ExploreUseCase(getRepository()) }
    }

    fun providedSearchUseCase(): SearchUseCase {
        return SearchUseCase.create { SearchUseCase(getRepository()) }
    }
}

val LocalExploreUseCase = compositionLocalOf<ExploreUseCase> { error("Explore UseCase not provided") }
val LocalSearchUseCase = compositionLocalOf<SearchUseCase> { error("Search UseCase not provided") }