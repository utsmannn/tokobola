import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.utsman.tokobola.core.navigation.LocalScreenContainer
import com.utsman.tokobola.core.navigation.Navigation
import com.utsman.tokobola.core.navigation.ScreenContainer
import kotlinx.coroutines.flow.MutableStateFlow

class NavigationProvider : Navigation {

    private val navigatorStack: MutableStateFlow<Navigator?> = MutableStateFlow(null)
    private val screenContainer: MutableStateFlow<ScreenContainer?> = MutableStateFlow(null)

    @Composable
    fun initialize() {
        navigatorStack.value = LocalNavigator.current
        screenContainer.value = LocalScreenContainer.current
    }

    override fun goToDetail(id: Int): Boolean {
        return try {
            screenContainer.value?.detail(id)?.let { navigatorStack.value?.push(it) }
            true
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}