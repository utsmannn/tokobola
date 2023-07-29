package tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

internal object HomeTab : CustomTab {

    @Composable
    override fun Content() {
        val screenContainer = LocalScreenContainer.current
        screenContainer.home().Content()
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val painter = painterResource(SharedRes.images.icon_home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = painter
                )
            }
        }

    override val iconSelected: Painter?
        @Composable
        get() {
            return painterResource(SharedRes.images.icon_home_fill)
        }

}