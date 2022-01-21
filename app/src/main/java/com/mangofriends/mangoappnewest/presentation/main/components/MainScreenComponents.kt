package com.mangofriends.mangoappnewest.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.presentation.main.MainViewModel
import com.mangofriends.mangoappnewest.presentation.main.cards.TestStudyCardView
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink
import kotlinx.coroutines.launch


@Composable
fun BottomBar(onMessageClicked: () -> Unit, onSettingsClicked: () -> Unit) {
    // BottomAppBar Composable
    BottomAppBar(
        backgroundColor = Pink,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Filled.Pending,
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable(onClick = onMessageClicked),
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable(onClick = onSettingsClicked),
                tint = Color.White
            )
        }
    }
}


@Composable
fun Drawer(viewModel: MainViewModel, navController: NavController) {
    // Column Composable
    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(modifier = Modifier
            .clickable { viewModel.logOut(navController = navController) }
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text(
                text = stringResource(id = R.string.settings_log_out),
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Default.Logout, contentDescription = "", tint = Pink)
        }
    }
}


@ExperimentalCoilApi
@Composable
fun Body(viewModel: MainViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TestStudyCardView(
            viewModel = viewModel,
            loadProfilesHandler = { viewModel.getProfiles("uid") },
            sendLikeHandler = {
                viewModel.likeUser(
                    viewModel.uid,
                    viewModel.data.value[viewModel.topCardIndex.value].user.uid
                )
            })
    }
}

@ExperimentalCoilApi
@Composable
fun ScaffoldExample(viewModel: MainViewModel, navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    // create a scaffold state, set it to close by default
    val scaffoldState = rememberScaffoldState(drawerState)

    // Create a coroutine scope. Opening of
    // Drawer and snackbar should happen in
    // background thread without blocking main thread
    val coroutineScope = rememberCoroutineScope()

    // Scaffold Composable
    Scaffold(

        // pass the scaffold state
        scaffoldState = scaffoldState,
        drawerShape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 0.dp),
        bottomBar = {
            BottomBar(
                onMessageClicked = {
                    coroutineScope.launch {
                        // to close use -> scaffoldState.drawerState.close()
                        scaffoldState.drawerState.open()
                    }
                },
                onSettingsClicked = {
                    coroutineScope.launch {
                        // to close use -> scaffoldState.drawerState.close()
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },

        content = { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                if (viewModel.state.value.isLoading)
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                else
                    Body(viewModel)
            }

        },

        // pass the drawer
        drawerContent = {
            Drawer(viewModel = viewModel, navController = navController)
        },
        drawerGesturesEnabled = drawerState.isOpen
    )

}