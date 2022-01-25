package com.mangofriends.mangoappnewest.presentation.main

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.presentation.main.components.MainScaffold

@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val activity = context as Activity
    val warningText = stringResource(id = R.string.warning_press_back_to_exit)

    Box(modifier = Modifier.fillMaxSize()) {
        BackHandler() {
            if (viewModel.isBackExit()) activity.finish() else
                Toast.makeText(context, warningText, Toast.LENGTH_LONG).show()
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            MainScaffold(viewModel, navController)
        }
    }
}