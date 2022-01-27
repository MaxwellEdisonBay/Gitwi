package com.mangofriends.mangoappnewest.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.presentation.register.components.*

@ExperimentalFoundationApi
@Composable
fun Step1(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = { Next1Button(viewModel = viewModel) },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Step1Body(viewModel = viewModel)
    }


}

@ExperimentalFoundationApi
@Composable
fun Step1Body(viewModel: RegisterViewModel) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {

                focusManager.clearFocus()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            MainRegLabel()
            NameTextField(viewModel = viewModel)
            EmailTextField(viewModel = viewModel)
            PasswordTextField(viewModel = viewModel)
            if (LocalConfiguration.current.screenHeightDp > Constants.SKIP_REGISTER_ANIMATION_MIN_DP)
                RegisterAnimation()

        }
    }
}