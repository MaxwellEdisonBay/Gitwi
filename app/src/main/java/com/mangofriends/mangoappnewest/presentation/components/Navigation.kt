package com.mangofriends.mangoappnewest.presentation.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.presentation.chat.components.ChatScreen
import com.mangofriends.mangoappnewest.presentation.main.components.MainScreen
import com.mangofriends.mangoappnewest.presentation.login.components.LoginScreen

@Composable
fun Navigation() {
    Surface(color = MaterialTheme.colors.background) {


        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {

            composable(route = Screen.LoginScreen.route) {
                LoginScreen(navController)
            }
            composable(
                route = Screen.MainScreen.route + "/{"+Constants.PARAM_UID+"}",
                arguments = listOf(navArgument(Constants.PARAM_UID) {
                    type = NavType.StringType
                    defaultValue = "Maxwell"
                    nullable = true
                })
            ) {
                MainScreen(uid = it.arguments?.getString(Constants.PARAM_UID), navController =  navController)
            }
            composable(
                route = Screen.ChatScreen.route + "/{"+Constants.PARAM_UID+"}",
                arguments = listOf(navArgument(Constants.PARAM_UID) {
                    type = NavType.StringType
                    defaultValue = "Maxwell"
                    nullable = true
                })
            ) {
                ChatScreen(uid = it.arguments?.getString(Constants.PARAM_UID), navController)
            }
        }
    }
}

