import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.seiko.imageloader.LocalImageLoader
import com.utsman.tokobola.core.appImageLoader
import com.utsman.tokobola.home.HomeInstanceProvider
import com.utsman.tokobola.home.LocalHomeUseCase
import com.utsman.tokobola.home.view.Home
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {
    CompositionLocalProvider(
        LocalHomeUseCase provides HomeInstanceProvider.providedUseCase(),
        LocalImageLoader provides appImageLoader()
    ) {
        MaterialTheme {
            Home()
        }
    }
}

expect fun getPlatformName(): String