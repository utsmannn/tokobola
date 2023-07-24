import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App()

@Composable
fun back() {
    BackHandler(true) {
        println("asuuuuuuuuuuu")
    }
}
