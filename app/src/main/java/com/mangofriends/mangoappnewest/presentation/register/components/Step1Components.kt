package com.mangofriends.mangoappnewest.presentation.register.components

import android.content.res.Resources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
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
import com.airbnb.lottie.compose.*
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.register.RegisterViewModel
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EmailTextField(viewModel: RegisterViewModel) {
    val emailState = remember { viewModel.state.emailState }
    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            value = emailState.text,
            onValueChange = {
                emailState.onChanged(it)
                emailState.validate()
            },
            label = { Text(text = stringResource(id = R.string.email_hint)) },
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
        modifier = Modifier.fillMaxWidth(0.7f),
        style = TextStyle(color = MaterialTheme.colors.error)
    )
}

@Composable
fun NameTextField(viewModel: RegisterViewModel) {
    val nameState = remember { viewModel.state.nameState }
    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            value = nameState.text,
            onValueChange = {
                nameState.onChanged(it)
                nameState.validate()
            },
            label = { Text(text = stringResource(id = R.string.name_hint)) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            isError = nameState.error != null,

            )
        nameState.error?.let {
            ErrorField(it)
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun PasswordTextField(viewModel: RegisterViewModel) {
    val showPassword1 = remember { mutableStateOf(false) }
    val showPassword2 = remember { mutableStateOf(false) }
    val password1State = remember { viewModel.state.password1State }
    val password2State = remember { viewModel.state.password2State }
    val matchPassErr = stringResource(id = R.string.error_same_password)
    val relocation = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    if (password1State.text != password2State.text && !(password1State.text.isBlank() && password2State.text.isBlank()))
        password2State.error = matchPassErr
    else
        password2State.error = null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.7f),
                value = password1State.text,
                onValueChange = {
                    password1State.onChanged(it)
                    password1State.validate()
                },
                label = { Text(text = stringResource(id = R.string.password_hint)) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium,
                visualTransformation = if (showPassword1.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    if (showPassword1.value) {
                        IconButton(onClick = { showPassword1.value = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.hide_password)
                            )
                        }

                    } else {
                        IconButton(onClick = { showPassword1.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.show_password)
                            )
                        }
                    }
                },
                isError = password1State.error != null
            )
            password1State.error?.let { ErrorField(it) }

        }
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .bringIntoViewRequester(relocation)
                    .onFocusEvent {
                        if (it.isFocused) scope.launch { delay(200); relocation.bringIntoView() }
                    },
                value = password2State.text,
                onValueChange = {
                    password2State.onChanged(it)
                    password2State.validate()
                },
                label = { Text(text = stringResource(id = R.string.password_repeat_hint)) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium,
                visualTransformation = if (showPassword2.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    if (showPassword2.value) {
                        IconButton(onClick = { showPassword2.value = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.hide_password)
                            )
                        }

                    } else {
                        IconButton(onClick = { showPassword2.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.show_password)
                            )
                        }
                    }
                },
                isError = password2State.error != null
            )

        }
        password2State.error?.let { ErrorField(it) }
    }
}

@Composable
fun MainRegLabel() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
            text = stringResource(id = R.string.reg_title_1),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center

        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(id = R.string.reg_title_2),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun Next1Button(viewModel: RegisterViewModel) {
    val focusManager = LocalFocusManager.current
    val res = LocalContext.current.resources
    val inputErr = stringResource(id = R.string.error_msg_incorrect_input)
    Surface(elevation = 5.dp, modifier = Modifier.size(50.dp), shape = CircleShape) {
        OutlinedButton(
            onClick = {
                focusManager.clearFocus()
                if (step1ErrorHandler(viewModel, res)) {
                    viewModel.moveToPage(Screen.RegisterStep2.route, false)
                } else {
                    viewModel.state.error.value = inputErr
                }
            },
            border = BorderStroke(0.dp, Color.Transparent),
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(start = 5.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Pink)
        ) {
            Icon(Icons.Default.NavigateNext, contentDescription = "Next", tint = Color.White)
        }
    }
}

@Composable
fun RegisterAnimation() {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hand_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        restartOnPlay = true,
    )
    LottieAnimation(
        composition,
        progress,
        modifier = Modifier.requiredSize(200.dp)
    )
}

fun step1ErrorHandler(viewModel: RegisterViewModel, resources: Resources): Boolean {
    val state = viewModel.state
    val errReq = resources.getString(R.string.error_fill_this_field)

    if (state.nameState.text.isBlank())
        state.nameState.error = errReq
    if (state.emailState.text.isBlank())
        state.emailState.error = errReq
    if (state.password1State.text.isBlank())
        state.password1State.error = errReq
    if (state.password2State.text.isBlank())
        state.password2State.error = errReq

    return state.emailState.error == null
            && state.password1State.error == null
            && state.nameState.error == null
            && state.password2State.error == null
}

