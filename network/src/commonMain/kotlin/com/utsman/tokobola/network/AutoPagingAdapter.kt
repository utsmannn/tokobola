package com.utsman.tokobola.network

import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.network.response.BasePagedResponse


class AutoPagingAdapter<U>(val pagingReducer: ApiReducer<Paged<U>>) {

    val listPaged: MutableList<U> = mutableListOf()
    var currentPage: Int = 1
    var prevPage: Int = 1
    var hasNextPage = true

    suspend inline fun <reified T>executeResponse(crossinline call: suspend (Int) -> BasePagedResponse<T>, crossinline mapper: (T) -> U) {
        if (hasNextPage) {
            pagingReducer.transform(
                call = {
                    val pagingResponse = call.invoke(currentPage).also {
                        hasNextPage = it.data?.hasNextPage.orFalse()
                        prevPage = currentPage
                    }
                    pagingResponse
                },
                mapper = { pagedResponse ->
                    val dataPaged = pagedResponse.data
                    val dataList = dataPaged?.data.orEmpty()
                        .map(mapper)

                    currentPage = dataPaged?.page.orNol() + 1

                    listPaged.addAll(dataList)
                    Paged(
                        data = listPaged,
                        hasNextPage = dataPaged?.hasNextPage.orFalse(),
                        page = dataPaged?.page ?: 1,
                        perPage = dataPaged?.perPage ?: 10
                    )
                }
            )
        } else {
            println("End of reach paging!")
        }
    }

    fun clear() {
        currentPage = 1
        prevPage = 0
        hasNextPage = true
        listPaged.clear()
        pagingReducer.clear()
    }
}