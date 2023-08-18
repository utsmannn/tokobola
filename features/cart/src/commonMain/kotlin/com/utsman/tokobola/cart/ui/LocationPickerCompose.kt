package com.utsman.tokobola.cart.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.common.component.MapView
import com.utsman.tokobola.common.component.rememberMapConfigState
import com.utsman.tokobola.core.data.LatLon

@Composable
fun LocationPicker() {
    val mapConfigState = rememberMapConfigState()

    val currentLatLon by derivedStateOf { mapConfigState.currentLatLon }

    var centerLatLon by mutableStateOf(LatLon())

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            MapView(
                modifier = Modifier.fillMaxSize(),
                mapConfigState = mapConfigState
            )

            Column(
                modifier = Modifier.padding(120.dp)
            ) {
                Text(
                    text = "$currentLatLon"
                )

                Button(
                    onClick = {
                        centerLatLon = mapConfigState.getCenterLocation()
                    }
                ) {
                    Text("get center latlon $centerLatLon")
                }

                Button(
                    onClick = {
                        mapConfigState.zoomIn()
                    }
                ) {
                    Text("zoom in")
                }

                Button(
                    onClick = {
                        mapConfigState.zoomOut()
                    }
                ) {
                    Text("zoom out")
                }

                Button(
                    onClick = {
                        mapConfigState.setLocation(LatLon(-6.200000, 106.816666))
                    }
                ) {
                    Text("set loc")
                }
            }
        }
    }
}