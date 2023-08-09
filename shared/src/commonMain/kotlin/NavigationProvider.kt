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

    override fun back(): Boolean {
        return tryAction { it.pop() }
    }

    override fun goToDetail(id: Int): Boolean {
        return tryAction { nav ->
            screenContainer.value?.detail(id)?.let { nav.push(it) }
        }
    }

    override fun goToSearch(): Boolean {
        return tryAction { nav ->
            screenContainer.value?.search()?.let { nav.push(it) }
        }
    }

    private fun tryAction(action: (Navigator) -> Unit): Boolean {
        return try {
            navigatorStack.value?.let(action)
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