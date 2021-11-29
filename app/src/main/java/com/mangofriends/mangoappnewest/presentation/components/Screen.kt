package com.mangofriends.mangoappnewest.presentation.components

sealed class Screen(val route: String){
    object LoginScreen : Screen("login_screen")
    object MainScreen : Screen("main_screen")
    object ChatScreen : Screen("chat_screen")

    fun withArgs(vararg args: String):String{
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}
