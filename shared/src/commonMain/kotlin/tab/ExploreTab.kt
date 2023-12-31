package tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

internal object ExploreTab : CustomTab {

    @Composable
    override fun Content() {
        val screenContainer = LocalScreenContainer.current
        screenContainer.explore().Content()
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "Explorer"
            val painter = painterResource(SharedRes.images.icon_explore)
            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = painter
                )
            }
        }

    override val iconSelected: Painter?
        @Composable
        get() {
            return painterResource(SharedRes.images.icon_explore_fill)
        }
}