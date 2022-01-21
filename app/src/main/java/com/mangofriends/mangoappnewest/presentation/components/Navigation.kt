package com.mangofriends.mangoappnewest.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.presentation.chat.components.ChatScreen
import com.mangofriends.mangoappnewest.presentation.login.LoginScreen
import com.mangofriends.mangoappnewest.presentation.main.MainScreen
import com.mangofriends.mangoappnewest.presentation.register.RegisterScreen

@ExperimentalComposeUiApi
@ExperimentalCoilApi
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
                route = Screen.MainScreen.route + "/{" + Constants.PARAM_UID + "}",
                arguments = listOf(navArgument(Constants.PARAM_UID) {
                    type = NavType.StringType
                    defaultValue = "Maxwell"
                    nullable = true
                })
            ) {
                MainScreen(
                    navController = navController
                )
            }
            composable(
                route = Screen.ChatScreen.route + "/{" + Constants.PARAM_UID + "}",
                arguments = listOf(navArgument(Constants.PARAM_UID) {
                    type = NavType.StringType
                    defaultValue = "Maxwell"
                    nullable = true
                })
            ) {
                ChatScreen(uid = it.arguments?.getString(Constants.PARAM_UID), navController)
            }

            composable(
                route = Screen.RegisterScreen.route
            ) {
                EnterAnimation {
                    RegisterScreen(navController)
                }
            }
        }
    }
}



