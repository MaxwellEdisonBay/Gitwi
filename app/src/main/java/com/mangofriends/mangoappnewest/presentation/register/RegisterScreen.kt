package com.mangofriends.mangoappnewest.presentation.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.presentation.components.CrossSlide
import com.mangofriends.mangoappnewest.presentation.components.Screen

@ExperimentalMaterialApi
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val currentPage by remember { viewModel.animationState.currentPage }

    Box(modifier = Modifier.fillMaxSize()) {
        if (viewModel.state.isLoading.value) {
            Surface(color = Color.White.copy(alpha = 0.5f)) {}
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            CrossSlide(targetState = currentPage) { screen ->
                when (screen) {
                    Screen.RegisterStep1.route -> {
                        Step1(navController = navController)
                    }
                    Screen.RegisterStep2.route -> {
                        Step2()
                    }
                    Screen.RegisterStep3.route -> {
                        Step3(navController = navController)
                    }
                }
            }
        }

    }

}
