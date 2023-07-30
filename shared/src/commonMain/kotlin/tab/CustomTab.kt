package tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.navigator.tab.Tab

internal interface CustomTab : Tab {

    val iconSelected: Painter?
        @Composable
        get() = options.icon
}