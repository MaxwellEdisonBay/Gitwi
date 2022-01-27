package com.mangofriends.mangoappnewest.presentation.main.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.main.MainViewModel
import com.mangofriends.mangoappnewest.presentation.main.cards.CardView
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink
import kotlinx.coroutines.launch


@Composable
fun BottomBar(onMessageClicked: () -> Unit, onSettingsClicked: () -> Unit) {

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


@ExperimentalCoilApi
@Composable
fun Drawer(viewModel: MainViewModel, navController: NavController) {
    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val currentUser by remember { viewModel.currentUser }
            val profiles =
                currentUser.matches.values.toList().sortedByDescending { it.last_message_time }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imagePainter =
                    rememberImagePainter(data = viewModel.currentUser.value.profile_image_urls.values.toList()[0].url)
                Image(
                    painter = imagePainter,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = viewModel.currentUser.value.name + stringResource(id = R.string.drawer_title),
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Start
                )

            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(profiles.asReversed()) { index, profile ->
                    ProfilesListItem(
                        profile = profile
                    ) {
                        navController.currentBackStackEntry!!.arguments!!.putParcelable(
                            "match",
                            profile
                        )
//                        navController.currentBackStackEntry!!.arguments!!.putString("my_photo",currentUser.profile_image_urls.values.toList()[0].url)
                        navController.currentBackStackEntry!!.arguments!!.putString(
                            "my_photo",
                            currentUser.profile_image_urls.values.toList()[0].url
                        )

                        Log.d("DEBUG", navController.currentBackStackEntry!!.arguments.toString())
                        navController.navigate(Screen.ChatScreen.route)
                    }
                    if (index < profiles.lastIndex)
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                        )
                }
            }
        }

        Row(modifier = Modifier
            .clickable { viewModel.logOut(navController = navController) }
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.settings_log_out),
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Start
            )
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "",
                tint = Pink,
            )
        }
    }
}


@ExperimentalCoilApi
@Composable
fun Body(viewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val notifText = stringResource(id = R.string.notification_new_match)
    val currentUser by remember { viewModel.currentUser }
    val users by remember { viewModel.state }
    var topCardIndex by remember { viewModel.topCardIndex }
    val usersList = users.toUserCardList()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        CardView(
            viewModel = viewModel,
            navController = navController,
            loadProfilesHandler = { viewModel.getProfiles() },
            sendLikeHandler = {
                viewModel.likeUser(
                    currentUser.uid,
                    usersList[topCardIndex].user.uid,
                    onLike = {
                        viewModel.checkMatch(
                            currentUser,
                            usersList[topCardIndex].user,
                            onMatch = {
                                if (it)
                                    Toast.makeText(context, notifText, Toast.LENGTH_SHORT).show()
                                if (topCardIndex < users.toUserCardList().lastIndex) {
                                    topCardIndex += 1
                                } else {
//                                    loadProfilesHandler.invoke()
                                    viewModel.getProfiles()
                                    topCardIndex = 0
                                }
                            })
                    }
                )
            })
    }
}

@ExperimentalCoilApi
@Composable
fun MainScaffold(viewModel: MainViewModel, navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scaffoldState = rememberScaffoldState(drawerState)

    val coroutineScope = rememberCoroutineScope()

    Scaffold(

        scaffoldState = scaffoldState,
        drawerShape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 0.dp),
        bottomBar = {
            BottomBar(
                onMessageClicked = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onSettingsClicked = {
                    coroutineScope.launch {
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
                Body(viewModel, navController)
            }

        },

        drawerContent = {
            Drawer(viewModel = viewModel, navController = navController)
        },
        drawerGesturesEnabled = drawerState.isOpen
    )

}