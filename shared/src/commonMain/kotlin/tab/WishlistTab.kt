package tab

import MapView
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

internal object WishlistTab : CustomTab {
    @Composable
    override fun Content() {
        val screenContainer = LocalScreenContainer.current
        screenContainer.wishlist().Content()
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "Wishlist"
            val painter = painterResource(SharedRes.images.icon_bookmark)
            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = painter
                )
            }
        }

    override val iconSelected: Painter?
        @Composable
        get() {
            return painterResource(SharedRes.images.icon_bookmark_fill)
        }
}