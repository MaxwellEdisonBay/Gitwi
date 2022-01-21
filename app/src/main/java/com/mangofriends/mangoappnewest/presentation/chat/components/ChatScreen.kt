package com.mangofriends.mangoappnewest.presentation.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ChatScreen(uid: String?, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Your UID is: ${uid!!}",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}