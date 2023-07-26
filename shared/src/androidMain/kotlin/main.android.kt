import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App()