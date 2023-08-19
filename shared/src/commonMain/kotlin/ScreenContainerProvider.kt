import cafe.adriel.voyager.core.screen.Screen
import com.utsman.tokobola.cart.ui.Cart
import com.utsman.tokobola.cart.ui.LocationPicker
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.core.navigation.ScreenContainer
import com.utsman.tokobola.core.navigation.screenContentOf
import com.utsman.tokobola.details.ui.product.ProductDetail
import com.utsman.tokobola.details.ui.brand.BrandDetail
import com.utsman.tokobola.details.ui.category.CategoryDetail
import com.utsman.tokobola.explore.ui.explore.Explore
import com.utsman.tokobola.explore.ui.search.Search
import com.utsman.tokobola.home.ui.Home
import com.utsman.tokobola.wishlist.ui.Wishlist

class ScreenContainerProvider : ScreenContainer {

    override fun home(): Screen = screenContentOf("home") {
        Home()
    }

    override fun detailProduct(productId: Int): Screen = screenContentOf("detail") {
        ProductDetail(productId)
    }

    override fun detailCategory(categoryId: Int) = screenContentOf("category_detail") {
        CategoryDetail(categoryId)
    }

    override fun detailBrand(brandId: Int) = screenContentOf("brand_detail") {
        BrandDetail(brandId)
    }

    override fun explore(): Screen = screenContentOf("explore") {
        Explore()
    }

    override fun wishlist(): Screen = screenContentOf("wishlist") {
        Wishlist()
    }

    override fun search(): Screen = screenContentOf("search") {
        Search()
    }

    override fun cart(): Screen = screenContentOf("cart") {
        Cart()
    }

    override fun locationPicker(latLon: LatLon) = screenContentOf("location_picker") {
        LocationPicker(latLon)
    }
}