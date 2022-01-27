package com.mangofriends.mangoappnewest.presentation.register

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.register.components.*
import kotlinx.coroutines.launch


@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Step2(
    viewModel: RegisterViewModel = hiltViewModel()
) {


    Scaffold(
        floatingActionButton = { Next2Button(viewModel) },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Step2Body(viewModel = viewModel)
    }
}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Step2Body(viewModel: RegisterViewModel) {
    val uriState by remember { mutableStateOf(viewModel.state.imageUriState) }
    val imagesList = remember { viewModel.listOfAllImages.asReversed() }
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = MaterialTheme.shapes.medium,
        scrimColor = Color.Black.copy(alpha = 0.12f),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultMinSize(minHeight = 1.dp)
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .align(Alignment.Center)
                            .fillMaxWidth(0.5f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(1.dp))
                    )
                }
                if (imagesList.isNotEmpty())
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(4),
                    ) {
                        items(imagesList.size) {
                            Card(
                                backgroundColor = Color.White,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .size(100.dp)
                                    .clickable {
                                        scope.launch {
                                            state.hide()
//                                            uriState.error = null
                                            uriState.value = ImageUriState(imagesList[it], null)
                                            Log.d(
                                                "DEBUGUID",
                                                viewModel.state.imageUriState.toString()
                                            )
                                        }
                                    },
                                elevation = 0.dp,
                                shape = RoundedCornerShape(0.dp)
                            ) {
                                Image(
                                    painter = rememberImagePainter(imagesList[it]),
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
            }
        }
    ) {
        Body(viewModel, onAddImage = {
            viewModel.listOfAllImages.forEach {
                Log.d("Invoke", it.toString())
            }
            scope.launch { state.show() }
        })
    }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun Body(viewModel: RegisterViewModel, onAddImage: () -> Unit) {
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
            UploadImage(viewModel, onAddImage = { onAddImage.invoke() })
            CityTextField(viewModel)
            AgeTextField(viewModel)
            GenderDropDown(viewModel)
            BioTextField(viewModel)

        }
    }
}


