import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.utsman.tokobola.core.utils.rememberNavigationBarHeightDp
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
                    .padding(bottom = rememberNavigationBarHeightDp()),
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

    @Composable
    private fun RowScope.TabNavigationItem(tab: CustomTab) {
        val tabNavigator = LocalTabNavigator.current

        val isSelected by derivedStateOf {
            tabNavigator.current == tab
        }


        BottomNavigationItem(
            selected = isSelected,
            onClick = { tabNavigator.current = tab },
            icon = {
                val defaultPainter = tab.options.icon
                val selectedPainter = tab.iconSelected ?: defaultPainter

                val animatedColorAccent by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colors.primary else Color.White
                )

                val animatedSizeIcon by animateDpAsState(
                    targetValue = if (isSelected) 20.dp else 28.dp
                )

                val painter = if (isSelected) selectedPainter else defaultPainter

                painter?.let {
                    val modifier = if (isSelected) {
                        Modifier
                            .background(color = Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(16.dp))
                    } else {
                        Modifier
                    }
                    Row(
                        modifier = modifier.padding(
                            horizontal = 6.dp,
                            vertical = 4.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = tab.options.title,
                            modifier = Modifier.size(animatedSizeIcon),
                            colorFilter = ColorFilter.tint(animatedColorAccent)
                        )

                        AnimatedVisibility(
                            visible = isSelected,
                        ) {
                            Text(
                                text = tab.options.title,
                                fontSize = 12.sp,
                                color = animatedColorAccent,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }


                }
            }
        )
    }
}