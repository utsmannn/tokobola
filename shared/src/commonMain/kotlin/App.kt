import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.seiko.imageloader.LocalImageLoader
import com.utsman.tokobola.common.theme.CommonTheme
import com.utsman.tokobola.core.appImageLoader
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.core.navigation.screenContentOf
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.details.DetailInstanceProvider
import com.utsman.tokobola.details.LocalDetailUseCase
import com.utsman.tokobola.home.HomeInstanceProvider
import com.utsman.tokobola.home.LocalHomeUseCase
import tab.AboutTab
import tab.ExploreTab
import tab.HomeTab
import tab.WishlistTab

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
            Navigator(tabMain()) {
                navigation.initialize()
                SlideTransition(it)
            }

        }
    }
}

private fun tabMain(): Screen {
    return screenContentOf("main") {
        TabNavigator(HomeTab) {
            Scaffold(
                bottomBar = {
                    BottomNavigation(
                        modifier = Modifier.padding(bottom = PlatformUtils.rememberNavigationBarHeight().dp)
                    ) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(ExploreTab)
                        TabNavigationItem(WishlistTab)
                        TabNavigationItem(AboutTab)
                    }
                }
            ) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let {
                Icon(painter = it, contentDescription = tab.options.title, modifier = Modifier.size(24.dp))
            }
        }
    )
}