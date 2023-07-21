package com.utsman.tokobola.core.data

data class Paged<T>(
    var data: List<T> = emptyList(),
    var hasNextPage: Boolean = false,
    var page: Int = 1,
    var perPage: Int = 10
)