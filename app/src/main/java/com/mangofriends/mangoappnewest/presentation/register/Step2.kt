package com.mangofriends.mangoappnewest.presentation.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.register.components.*


@Composable
fun Step2(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    BackHandler(onBack = {
        viewModel.moveToPage(Screen.RegisterStep1.route, true)
    })


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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            PersonalInfoLabel()
//            UploadPhoto()
            UploadImage(viewModel)
            CityTextField(viewModel)
            AgeTextField(viewModel)
            GenderDropDown(viewModel)
            BioTextField(viewModel)

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Next2Button(viewModel)
        }
    }
}

//
//var currentPage by remember { viewModel.currentPage }
//
//
//
//CrossSlide(targetState = currentPage, reverseAnimation = false) { screen ->
//    when (screen) {
//        "A" -> {
//            Step1(viewModel)
//
//
//        }
//        "B" -> {
//            Step2(viewModel)
//
//        }
//
//    }
//
//}
//}
//


