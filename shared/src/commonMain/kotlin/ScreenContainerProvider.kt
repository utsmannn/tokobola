import cafe.adriel.voyager.core.screen.Screen
import com.utsman.tokobola.core.navigation.ScreenContainer
import com.utsman.tokobola.core.navigation.screenContentOf
import com.utsman.tokobola.details.ui.Detail
import com.utsman.tokobola.details.ui.category.CategoryDetail
import com.utsman.tokobola.explore.ui.explore.Explore
import com.utsman.tokobola.explore.ui.search.Search
import com.utsman.tokobola.home.ui.Home

class ScreenContainerProvider : ScreenContainer {

    override fun home(): Screen = screenContentOf("home") {
        Home()
    }

    override fun detailProduct(productId: Int): Screen = screenContentOf("detail") {
        Detail(productId)
    }

    override fun detailCategory(categoryId: Int) = screenContentOf("category_detail") {
        CategoryDetail(categoryId)
    }
    override fun explore(): Screen = screenContentOf("explore") {
        Explore()
    }

    override fun search(): Screen = screenContentOf("search") {
        Search()
    }
}