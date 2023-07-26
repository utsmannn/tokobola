package com.utsman.tokobola

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentWindow = window
        WindowCompat.setDecorFitsSystemWindows(currentWindow, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
            MainView()
        }
    }
}