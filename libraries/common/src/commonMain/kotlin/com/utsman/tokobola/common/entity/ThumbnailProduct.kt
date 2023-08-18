package com.utsman.tokobola.common.entity

data class ThumbnailProduct(
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val category: ThumbnailCategory = ThumbnailCategory(),
    val brand: ThumbnailBrand = ThumbnailBrand(),
    val image: String = "",
    val promoted: Boolean = false
) {
    data class ThumbnailBrand(
        val id: Int = 0,
        val name: String = "",
        val logo: String = ""
    )

    data class ThumbnailCategory(
        val id: Int = 0,
        val name: String = "",
    )

}