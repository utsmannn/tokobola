import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.seiko.imageloader.LocalImageLoader
import com.utsman.tokobola.cart.CartInstanceProvider
import com.utsman.tokobola.cart.LocalCartUseCase
import com.utsman.tokobola.cart.LocalLocationPickerUseCase
import com.utsman.tokobola.common.theme.CommonTheme
import com.utsman.tokobola.core.rememberImageLoader
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.details.DetailInstanceProvider
import com.utsman.tokobola.details.LocalBrandDetailUseCase
import com.utsman.tokobola.details.LocalCategoryDetailUseCase
import com.utsman.tokobola.details.LocalProductDetailUseCase
import com.utsman.tokobola.explore.ExploreInstanceProvider
import com.utsman.tokobola.explore.LocalExploreUseCase
import com.utsman.tokobola.explore.LocalSearchUseCase
import com.utsman.tokobola.home.HomeInstanceProvider
import com.utsman.tokobola.home.LocalHomeUseCase
import com.utsman.tokobola.location.LocalLocationTrackerProvider
import com.utsman.tokobola.location.LocationInstanceProvider
import com.utsman.tokobola.wishlist.LocalWishlistUseCase
import com.utsman.tokobola.wishlist.WishlistInstanceProvider

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    val imageLoader = rememberImageLoader()
    val screenContainer = remember { ScreenContainerProvider() }
    val navigation = remember { NavigationProvider() }
    val locationProvider = remember { LocationInstanceProvider.providedLocationTrackerProvider() }
    locationProvider.bindComposable()

    val homeUseCase = remember { HomeInstanceProvider.providedUseCase() }

    val productDetailUseCase = remember { DetailInstanceProvider.providedProductDetailUseCase() }
    val categoryDetailUseCase = remember { DetailInstanceProvider.providedCategoryDetailUseCase() }
    val brandDetailUseCase = remember { DetailInstanceProvider.providedBrandDetailUseCase() }
    val wishlistUseCase = remember { WishlistInstanceProvider.providedUseCase() }

    val exploreUseCase = remember { ExploreInstanceProvider.providedExploreUseCase() }
    val searchUseCase = remember { ExploreInstanceProvider.providedSearchUseCase() }
    val cartUseCase = remember { CartInstanceProvider.providedCartUseCase(locationProvider) }
    val locationPickerUseCase = remember { CartInstanceProvider.providedLocationPickerUseCase() }


    CompositionLocalProvider(
        // core
        LocalImageLoader provides imageLoader,
        LocalScreenContainer provides screenContainer,
        LocalNavigation provides navigation,
        LocalLocationTrackerProvider provides locationProvider,

        // use case
        LocalProductDetailUseCase provides productDetailUseCase,
        LocalCategoryDetailUseCase provides categoryDetailUseCase,
        LocalBrandDetailUseCase provides brandDetailUseCase,
        LocalHomeUseCase provides homeUseCase,
        LocalExploreUseCase provides exploreUseCase,
        LocalSearchUseCase provides searchUseCase,
        LocalWishlistUseCase provides wishlistUseCase,
        LocalCartUseCase provides cartUseCase,
        LocalLocationPickerUseCase provides locationPickerUseCase
    ) {

        CommonTheme {
            Navigator(TabHost) {
                navigation.initialize()
                SlideTransition(it)
            }
        }
    }
}