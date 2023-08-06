import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.seiko.imageloader.LocalImageLoader
import com.utsman.tokobola.common.theme.CommonTheme
import com.utsman.tokobola.core.appImageLoader
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.details.DetailInstanceProvider
import com.utsman.tokobola.details.LocalDetailUseCase
import com.utsman.tokobola.explore.ExploreInstanceProvider
import com.utsman.tokobola.explore.LocalExploreUseCase
import com.utsman.tokobola.home.HomeInstanceProvider
import com.utsman.tokobola.home.LocalHomeUseCase

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    val screenContainer = remember { ScreenContainerProvider() }
    val navigation = remember { NavigationProvider() }

    val homeUseCase = remember { HomeInstanceProvider.providedUseCase() }
    val detailUseCase = remember { DetailInstanceProvider.providedUseCase() }
    val exploreUseCase = remember { ExploreInstanceProvider.providedUseCase() }


    CompositionLocalProvider(
        // core
        LocalImageLoader provides appImageLoader(),
        LocalScreenContainer provides screenContainer,
        LocalNavigation provides navigation,

        // use case
        LocalDetailUseCase provides detailUseCase,
        LocalHomeUseCase provides homeUseCase,
        LocalExploreUseCase provides exploreUseCase
    ) {

        CommonTheme {
            Navigator(TabHost) {
                navigation.initialize()
                SlideTransition(it)
            }
        }
    }
}