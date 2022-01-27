package com.mangofriends.mangoappnewest.presentation.register.components

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.register.GenderState
import com.mangofriends.mangoappnewest.presentation.register.ImageUriState
import com.mangofriends.mangoappnewest.presentation.register.RegisterViewModel
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PersonalInfoLabel() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
            text = stringResource(id = R.string.reg_title_personal_1),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center

        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(id = R.string.reg_title_personal_2),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}


@ExperimentalCoilApi
@Composable
fun UploadImage(viewModel: RegisterViewModel, onAddImage: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val noUserImage = ImageBitmap.imageResource(R.drawable.empty)

    val imageUri by remember { viewModel.state.imageUriState }

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            focusManager.clearFocus()
            viewModel.getAllShownImagesPath(context.contentResolver)
            onAddImage.invoke()
        } else {
            Log.d("Upload Image", "PERMISSION DENIED")
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when (imageUri.uri) {
            null ->
                Box(
                    modifier = Modifier.clickable(interactionSource = interactionSource,
                        indication = null,
                        onClick = {

                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) -> {
                                    focusManager.clearFocus()
                                    viewModel.getAllShownImagesPath(context.contentResolver)
                                    onAddImage.invoke()
                                    Log.d("INVOKE", "Invoked")
                                }
                                else -> {
                                    // Asking for permission
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }

                        })
                ) {

                    Image(
                        bitmap = noUserImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(LightPink)
                            .align(Alignment.TopEnd),
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add image",
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }


                }

            else ->
                Box(modifier = Modifier.clickable(onClick = {
                    viewModel.getAllShownImagesPath(context.contentResolver)
                    onAddImage.invoke()
                })) {
                    Image(
                        painter = rememberImagePainter(imageUri.uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(LightPink)
                            .align(Alignment.TopEnd),
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Add image",
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

        }
        imageUri.error?.let {
            ErrorField(it)
        }
    }
}

@Composable
fun Next2Button(viewModel: RegisterViewModel) {
    val focusManager = LocalFocusManager.current
    val resources = LocalContext.current.resources
    val inputErr = stringResource(id = R.string.error_msg_incorrect_input)
    Surface(elevation = 5.dp, modifier = Modifier.size(50.dp), shape = CircleShape) {
        OutlinedButton(
            onClick = {
                focusManager.clearFocus()
                if (step2ErrorHandler(viewModel, resources)) {
                    viewModel.moveToPage(Screen.RegisterStep3.route, false)
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
fun GenderDropDown(viewModel: RegisterViewModel) {
    var expanded by remember { mutableStateOf(false) }

    val list = stringArrayResource(id = R.array.gender_options)
    var selectedItem by remember { viewModel.state.genderState }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    Column() {


        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedItem.gender,
                enabled = false,
                onValueChange = { selectedItem = GenderState(it, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    }
                    .clickable { expanded = !expanded },
                label = { Text(text = stringResource(id = R.string.reg_personal_select_gender)) },
                trailingIcon = {
                    Icon(icon, "")
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                list.forEach { label ->
                    DropdownMenuItem(onClick = {
                        selectedItem = GenderState(label, null)
                        expanded = false
                    }) {
                        Text(text = label)

                    }
                }

            }

        }
        selectedItem.error?.let {
            ErrorField(it)
        }
    }
}

@Composable
fun CityTextField(viewModel: RegisterViewModel) {
    val cityState = remember { viewModel.state.cityState }
    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = cityState.text,
            onValueChange = {
                cityState.onChanged(it)
                cityState.validate()
            },
            label = { Text(text = stringResource(id = R.string.city_hint)) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            isError = cityState.error != null,

            )
        cityState.error?.let {
            ErrorField(it)
        }
    }


}


@ExperimentalFoundationApi
@Composable
fun BioTextField(viewModel: RegisterViewModel) {
    val bioState = remember { viewModel.state.bioState }
    val scroll = rememberScrollState(0)
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val relocation = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .defaultMinSize(minHeight = 70.dp)
                .bringIntoViewRequester(relocation)
                .onFocusEvent {
                    if (it.isFocused) scope.launch { delay(200); relocation.bringIntoView() }
                },
//            .imePadding()
            value = bioState.text,
            onValueChange = {
                bioState.onChanged(it)
                bioState.validate()
            },
            label = { Text(text = stringResource(id = R.string.bio_hint)) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            isError = bioState.error != null,

            )
        bioState.error?.let {
            ErrorField(it)
        }
    }


}

@Composable
fun AgeTextField(viewModel: RegisterViewModel) {
    val ageState = remember { viewModel.state.ageState }
    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = ageState.text,
            onValueChange = {
                ageState.onChanged(it)
                ageState.validate()
            },
            label = { Text(text = stringResource(id = R.string.age_hint)) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            isError = ageState.error != null,

            )
        ageState.error?.let {
            ErrorField(it)
        }
    }
}

fun step2ErrorHandler(viewModel: RegisterViewModel, resources: Resources): Boolean {
    val state = viewModel.state
    val errImg = resources.getString(R.string.error_add_profile_image)
    val errReq = resources.getString(R.string.error_fill_this_field)
    val errGend = resources.getString(R.string.error_pick_gender)
    if (state.imageUriState.value.uri == null)
        state.imageUriState.value = ImageUriState(null, errImg)
    if (state.cityState.text.isBlank())
        state.cityState.error = errReq
    if (state.ageState.text.isBlank())
        state.ageState.error = errReq
    if (state.bioState.text.isBlank())
        state.bioState.error = errReq
    if (state.genderState.value.gender == "-")
        state.genderState.value = GenderState("-", errGend)
    return state.cityState.error == null
            && state.imageUriState.value.error == null
            && state.ageState.error == null
            && state.bioState.error == null
            && state.genderState.value.error == null
}


