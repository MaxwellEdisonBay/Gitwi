package com.mangofriends.mangoappnewest.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBMatch
import com.mangofriends.mangoappnewest.presentation.chat.components.ChatScreen
import com.mangofriends.mangoappnewest.presentation.details.DetailsScreen
import com.mangofriends.mangoappnewest.presentation.login.LoginScreen
import com.mangofriends.mangoappnewest.presentation.main.MainScreen
import com.mangofriends.mangoappnewest.presentation.register.RegisterScreen

@ExperimentalFoundationApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun Navigation() {
    Surface(color = MaterialTheme.colors.background) {


        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {

            composable(route = Screen.LoginScreen.route) {
                LoginScreen(navController)
//                MainScreen(navController)

            }
            composable(
                route = Screen.MainScreen.route,
            ) {
                MainScreen(
                    navController = navController
                )
            }
            composable(
                route = Screen.ChatScreen.route,

                ) {
                val profile =
                    navController.previousBackStackEntry?.arguments?.getParcelable<FBMatch>("match")
                val myphoto = navController.previousBackStackEntry?.arguments?.getString("my_photo")
                if (profile != null && myphoto != null) {
                    ChatScreen(profile, myphoto, navController)
                }
            }

            composable(
                route = Screen.RegisterScreen.route
            ) {
                EnterAnimation {
                    RegisterScreen(navController)
                }
            }

            composable(
                route = Screen.DetailsScreen.route
            ) {
                val profile =
                    navController.previousBackStackEntry?.arguments?.getParcelable<DTOUserProfile>("profile")
                if (profile != null) {
                    DetailsScreen(profile, navController)
                }
            }
        }
    }
}



