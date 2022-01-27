package com.mangofriends.mangoappnewest.presentation.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.register.components.*

@ExperimentalMaterialApi
@Composable
fun Step3(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    BackHandler(onBack = {
        viewModel.moveToPage(Screen.RegisterStep2.route, true)
    })
    Scaffold(floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { Next3Button(viewModel, navController) }
    ) {
        Step3Body(viewModel = viewModel, navController = navController)
    }
}

@ExperimentalMaterialApi
@Composable
fun Step3Body(viewModel: RegisterViewModel, navController: NavController) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            )
            {

                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)

        ) {
            SearchInfoLabel()
            AgeSlider(viewModel)
            SearchCityTextField(viewModel)
            InterestTagSwitch(viewModel)

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Next3Button(viewModel, navController)
        }
    }
}