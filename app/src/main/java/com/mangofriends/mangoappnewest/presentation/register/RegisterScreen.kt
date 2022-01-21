package com.mangofriends.mangoappnewest.presentation.register

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.mangofriends.mangoappnewest.presentation.components.CrossSlide
import com.mangofriends.mangoappnewest.presentation.components.Screen

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val currentPage by remember { viewModel.animationState.currentPage }

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


//@Composable
//fun Step1(viewModel: RegisterViewModel) {
//    Column(modifier = Modifier.fillMaxSize(),) {
//        Text("Page A")
//        Button(onClick = {
//            if (viewModel.currentPage.value == "B") viewModel.currentPage.value =
//                "A" else viewModel.currentPage.value = "B"
//        }) {}
//    }
//}
//
//@Composable
//fun Step2(viewModel: RegisterViewModel) {
//    BackHandler(onBack = { viewModel.currentPage.value = "A" })
//    Column(modifier = Modifier.fillMaxSize()) {
//        Text("Page B")
//        Button(onClick = {
//            if (viewModel.currentPage.value == "B") viewModel.currentPage.value =
//                "A" else viewModel.currentPage.value = "B"
//        }) {}
//    }
//}
