import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.seiko.imageloader.LocalImageLoader
import com.utsman.tokobola.common.theme.CommonTheme
import com.utsman.tokobola.core.appImageLoader
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.details.DetailInstanceProvider
import com.utsman.tokobola.details.LocalDetailUseCase
import com.utsman.tokobola.home.HomeInstanceProvider
import com.utsman.tokobola.home.LocalHomeUseCase

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    val homeUseCase = remember { HomeInstanceProvider.providedUseCase() }
    val detailUseCase = remember { DetailInstanceProvider.providedUseCase() }
    val screenContainer = remember { ScreenContainerProvider() }
    val navigation = remember { NavigationProvider() }

    CompositionLocalProvider(
        LocalHomeUseCase provides homeUseCase,
        LocalDetailUseCase provides detailUseCase,
        LocalImageLoader provides appImageLoader(),
        LocalScreenContainer provides screenContainer,
        LocalNavigation provides navigation
    ) {

        CommonTheme {
            Navigator(screenContainer.home()) {
                navigation.initialize()
                SlideTransition(it)
            }
        }
    }
}

expect fun getPlatformName(): String