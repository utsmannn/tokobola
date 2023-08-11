import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.seiko.imageloader.LocalImageLoader
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    val imageLoader = rememberImageLoader()
    val screenContainer = remember { ScreenContainerProvider() }
    val navigation = remember { NavigationProvider() }

    val homeUseCase = remember { HomeInstanceProvider.providedUseCase() }

    val productDetailUseCase = remember { DetailInstanceProvider.providedProductDetailUseCase() }
    val categoryDetailUseCase = remember { DetailInstanceProvider.providedCategoryDetailUseCase() }
    val brandDetailUseCase = remember { DetailInstanceProvider.providedBrandDetailUseCase() }

    val exploreUseCase = remember { ExploreInstanceProvider.providedExploreUseCase() }
    val searchUseCase = remember { ExploreInstanceProvider.providedSearchUseCase() }


    CompositionLocalProvider(
        // core
        LocalImageLoader provides imageLoader,
        LocalScreenContainer provides screenContainer,
        LocalNavigation provides navigation,

        // use case
        LocalProductDetailUseCase provides productDetailUseCase,
        LocalCategoryDetailUseCase provides categoryDetailUseCase,
        LocalBrandDetailUseCase provides brandDetailUseCase,

        LocalHomeUseCase provides homeUseCase,
        LocalExploreUseCase provides exploreUseCase,
        LocalSearchUseCase provides searchUseCase
    ) {

        CommonTheme {
            Navigator(TabHost) {
                navigation.initialize()
                SlideTransition(it)
            }
        }
    }
}