package com.mangofriends.mangoappnewest.presentation.register.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.register.RegisterViewModel
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink

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


@Composable
fun UploadImage(viewModel: RegisterViewModel) {
    val noUserImage = ImageBitmap.imageResource(R.drawable.empty)

    var imageUri by remember {
        viewModel.state.imageUriState
    }

    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column() {
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

        }
        when (bitmap.value) {
            null ->
                Box(modifier = Modifier.clickable(onClick = { launcher.launch("image/*") })) {

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
                Box(modifier = Modifier.clickable(onClick = { launcher.launch("image/*") })) {
                    Image(
                        bitmap = bitmap.value!!.asImageBitmap(),
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
    }


}

@Composable
fun Next2Button(viewModel: RegisterViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Surface(elevation = 5.dp, modifier = Modifier.size(50.dp), shape = CircleShape) {
        OutlinedButton(
            onClick = {
                focusManager.clearFocus()
                if (viewModel.isData2Correct()) {
                    viewModel.moveToPage(Screen.RegisterStep3.route, false)
                } else {
                    Toast.makeText(context, R.string.error_msg_incorrect_input, Toast.LENGTH_SHORT)
                        .show()
                }
//                viewModel.moveToPage(Screen.RegisterStep3.route, false)
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
    Column(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedItem,
            enabled = false,
            onValueChange = { selectedItem = it },
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
                    selectedItem = label
                    expanded = false
                }) {
                    Text(text = label)

                }
            }

        }

    }
}

@Composable
fun CityTextField(viewModel: RegisterViewModel) {
    val cityState = remember { viewModel.state.cityState }
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


@Composable
fun BioTextField(viewModel: RegisterViewModel) {
    val bioState = remember { viewModel.state.bioState }
    val scroll = rememberScrollState(0)
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultMinSize(minHeight = 70.dp),
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

@Composable
fun AgeTextField(viewModel: RegisterViewModel) {
    val ageState = remember { viewModel.state.ageState }
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


