package com.example.ketekura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ketekura.ui.theme.KetekuraTheme
import com.example.ketekura.view.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KetekuraTheme {
                MainScreen()
            }
        }
    }
}