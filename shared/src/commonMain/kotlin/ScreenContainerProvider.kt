import cafe.adriel.voyager.core.screen.Screen
import com.utsman.tokobola.core.navigation.ScreenContainer
import com.utsman.tokobola.core.navigation.screenContentOf
import com.utsman.tokobola.details.ui.Detail
import com.utsman.tokobola.home.ui.Home

class ScreenContainerProvider : ScreenContainer {

    override fun home(): Screen = screenContentOf("home") {
        Home()
    }

    override fun detail(productId: Int): Screen = screenContentOf("detail") {
        Detail(productId)
    }
}