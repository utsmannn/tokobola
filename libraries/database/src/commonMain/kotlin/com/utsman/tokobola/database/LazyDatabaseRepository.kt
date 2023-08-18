package com.utsman.tokobola.database

fun localRepository(): Lazy<LocalRepository> {
    return lazy(
        mode = LazyThreadSafetyMode.SYNCHRONIZED
    ) {
        LocalRepository.providedLocalRepository()
    }
}