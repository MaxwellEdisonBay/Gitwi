package com.mangofriends.mangoappnewest.presentation.components

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object MainScreen : Screen("main_screen")
    object ChatScreen : Screen("chat_screen")
    object RegisterScreen : Screen("register_screen")
    object RegisterStep1 : Screen("register_step1")
    object RegisterStep2 : Screen("register_step2")
    object RegisterStep3 : Screen("register_step3")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}
