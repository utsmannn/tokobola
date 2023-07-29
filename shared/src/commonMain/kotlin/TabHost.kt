import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.utsman.tokobola.core.utils.PlatformUtils
import tab.AboutTab
import tab.CustomTab
import tab.ExploreTab
import tab.HomeTab
import tab.WishlistTab

object TabHost : Screen {

    override val key: ScreenKey
        get() = "main_host"

    @Composable
    override fun Content() {
        TabNavigator(HomeTab) {
            Scaffold(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.primarySurface)
                    .padding(bottom = PlatformUtils.rememberNavigationBarHeight().dp),
                bottomBar = {
                    BottomNavigation {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(ExploreTab)
                        TabNavigationItem(WishlistTab)
                        TabNavigationItem(AboutTab)
                    }
                }
            ) { CurrentTab() }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun RowScope.TabNavigationItem(tab: CustomTab) {
        val tabNavigator = LocalTabNavigator.current

        val isSelected by derivedStateOf {
            tabNavigator.current == tab
        }


        BottomNavigationItem(
            selected = isSelected,
            onClick = { tabNavigator.current = tab },
            label = {
                    Text(
                        text = tab.options.title,
                        fontSize = 12.sp
                    )
            },
            alwaysShowLabel = true,
            icon = {
                val defaultPainter = tab.options.icon
                val selectedPainter = tab.iconSelected ?: defaultPainter

                AnimatedContent(
                    targetState = if (isSelected) selectedPainter else defaultPainter,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) {
                    it?.let {
                        Icon(
                            painter = it,
                            contentDescription = tab.options.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                /*val iconPainter = if (isSelected) selectedPainter else defaultPainter
                iconPainter?.let {
                    Icon(
                        painter = it,
                        contentDescription = tab.options.title,
                        modifier = Modifier.size(24.dp)
                    )
                }*/
            }
        )
    }
}