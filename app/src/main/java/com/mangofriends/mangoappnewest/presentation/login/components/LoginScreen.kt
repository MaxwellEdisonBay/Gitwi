package com.mangofriends.mangoappnewest.presentation.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.presentation.components.Screen

@Composable
fun LoginScreen(navController: NavController) {
    var name by remember {
        mutableStateOf("")
    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(
//            onClick = {navController.navigate(Screen.MainScreen.withArgs(name)) },
            onClick = {navController.navigate(Screen.MainScreen.withArgs(name)) },

            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Sign in")
        }
    }
}


