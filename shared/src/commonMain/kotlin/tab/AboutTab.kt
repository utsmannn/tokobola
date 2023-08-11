package tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource


/**
 * must be internal
 * https://github.com/JetBrains/compose-multiplatform/issues/3175#issuecomment-1564546150
 * */
internal object AboutTab : CustomTab {

    @Composable
    override fun Content() {
        val screenContainer = LocalScreenContainer.current
        val navigation = LocalNavigation.current
        Text("about", modifier = Modifier.padding(100.dp).clickable {
            navigation.goToDetailProduct(3)
        })
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "About"
            val painter = painterResource(SharedRes.images.icon_code)
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
            return painterResource(SharedRes.images.icon_code_fill)
        }
}