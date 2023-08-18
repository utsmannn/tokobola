import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

fun MainViewController() = ComposeUIViewController { App() }

@Composable
actual fun MapView() {
    val location = CLLocationCoordinate2DMake(-6.121435, 106.774124)
    val annotation = remember {
        MKPointAnnotation(
            location,
            title = "jekardah",
            subtitle = null
        )
    }
    val mkMapView = remember { MKMapView().apply { addAnnotation(annotation) } }

    val mapview = remember {
    }

    DisposableEffect(mkMapView) {
        onDispose {
            mkMapView.centerCoordinate().useContents {
                println("asuuuuu -> ${this.latitude} | ${this.longitude}")
            }
        }
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mkMapView
        },
        update = {
            mkMapView.setRegion(
                MKCoordinateRegionMakeWithDistance(
                    centerCoordinate = location,
                    10_000.0, 10_000.0
                ),
                animated = false
            )
        }
    )
}