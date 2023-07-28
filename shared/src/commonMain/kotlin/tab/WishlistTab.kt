package tab

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

object WishlistTab : Tab {
    @Composable
    override fun Content() {
        val screenContainer = LocalScreenContainer.current
        Text("wishlist")
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "Wishlist"
            val painter = painterResource(SharedRes.images.icon_bookmark_fill)
            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = painter
                )
            }
        }
}