package com.mangofriends.mangoappnewest.presentation.login.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.login.LoginViewModel
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink


@Composable
fun LoginScreenLabel() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.padding(bottom = 20.dp, top = 20.dp),
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center

        )
    }
}

@Composable
fun SignInButton(viewModel: LoginViewModel, navController: NavController) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Button(
        onClick = {
            focusManager.clearFocus()
            if (viewModel.isUserDataCorrect()) {
                val credentials =
                    UserCredentials(viewModel.emailState.text, viewModel.passwordState.text)
                viewModel.performLoginWithCredentials(credentials, navController, onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })

            } else {
                Toast.makeText(context, R.string.error_msg_incorrect_input, Toast.LENGTH_SHORT)
                    .show()
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Pink,
            contentColor = Color.White
        )
    ) {
        Text(
            text = stringResource(id = R.string.button_text)
        )
    }
}

@Composable
fun RegisterText(navController: NavController) {
    Text(
        text = stringResource(id = R.string.register_text),
        modifier = Modifier.clickable {
            navController.navigate(Screen.RegisterScreen.route)
        }
    )
}


@Composable
fun EmailTextField(viewModel: LoginViewModel) {
    val emailState = remember { viewModel.emailState }
    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = emailState.text,
            onValueChange = {
                emailState.onChanged(it)
                emailState.validate()
            },
            label = { Text(text = stringResource(id = R.string.login_hint)) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = MaterialTheme.shapes.medium,
            isError = emailState.error != null,

            )
        emailState.error?.let {
            ErrorField(it)
        }
    }
}

@Composable
fun ErrorField(error: String) {
    Text(
        text = error,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(color = MaterialTheme.colors.error)
    )
}

@Composable
fun PasswordTextField(viewModel: LoginViewModel) {
    val showPassword = remember { mutableStateOf(false) }
    val passwordState = remember { viewModel.passwordState }
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = passwordState.text,
        onValueChange = {
            passwordState.onChanged(it)
            passwordState.validate()
        },
        label = { Text(text = stringResource(id = R.string.password_hint)) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.medium,
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.hide_password)
                    )
                }

            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.show_password)
                    )
                }
            }
        },
        isError = passwordState.error != null
    )
    passwordState.error?.let { ErrorField(it) }
}

@Composable
fun LoginAnimation() {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.girl_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        restartOnPlay = true,
    )
    LottieAnimation(
        composition,
        progress,
        modifier = Modifier.requiredHeight(250.dp)
    )
}