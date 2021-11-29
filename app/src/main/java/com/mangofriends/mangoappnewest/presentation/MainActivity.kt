package com.mangofriends.mangoappnewest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mangofriends.mangoappnewest.presentation.components.Navigation
import com.mangofriends.mangoappnewest.presentation.ui.theme.MangoAppNewestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangoAppNewestTheme {
                Navigation()
            }
        }
    }
}
