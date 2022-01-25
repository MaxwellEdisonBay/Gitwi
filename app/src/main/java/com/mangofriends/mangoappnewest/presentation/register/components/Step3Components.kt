package com.mangofriends.mangoappnewest.presentation.register.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.presentation.register.RegisterViewModel
import com.mangofriends.mangoappnewest.presentation.ui.components.MngSwitch
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink
import kotlin.math.roundToInt


@Composable
fun SearchInfoLabel() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
            text = stringResource(id = R.string.reg_title_search_1),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center

        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(id = R.string.reg_title_search_2),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun Next3Button(viewModel: RegisterViewModel, navController: NavController) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("REGISTRATION", "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("REGISTRATION", "PERMISSION DENIED")
        }
    }
    Surface(elevation = 5.dp, modifier = Modifier.size(50.dp), shape = CircleShape) {
        OutlinedButton(
            onClick = {
                focusManager.clearFocus()
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.INTERNET
                    ) -> {
                        // Some works that require permission
                        Log.d("REGISTRATION", "Code requires permission")
                    }
                    else -> {
                        // Asking for permission
                        launcher.launch(Manifest.permission.INTERNET)
                    }
                }
                if (viewModel.isData3Correct()) {
                    viewModel.performRegister(viewModel, navController, onError = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    })
                } else {
                    Toast.makeText(context, R.string.error_msg_incorrect_input, Toast.LENGTH_SHORT)
                        .show()
                }
//                viewModel.performRegister(viewModel, navController, onError = {
//                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//                })

            },
            border = BorderStroke(0.dp, Color.Transparent),
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(start = 5.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Pink)
        ) {
            Icon(Icons.Default.Send, contentDescription = "Next", tint = Color.White)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun AgeSlider(viewModel: RegisterViewModel) {
    var sliderPosition by remember { viewModel.state.searchAgeSliderPositionState }
    val maxAge = sliderPosition.endInclusive.roundToInt()
    val maxText = if (maxAge == 50)
        "50+"
    else
        maxAge.toString()

    Column {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = sliderPosition.start.roundToInt().toString(),
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.reg_search_age_hint),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,

                )
            Text(
                text = maxText,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center
            )

        }

        RangeSlider(
            values = sliderPosition,
            valueRange = Constants.MIN_AGE..Constants.MAX_AGE,
            onValueChangeFinished = {
                viewModel.state.searchMinAgeState.value = sliderPosition.start
                viewModel.state.searchMaxAgeState.value = sliderPosition.endInclusive
            },
            onValueChange = { sliderPosition = it },
        )
    }
}

@Composable
fun SearchCityTextField(viewModel: RegisterViewModel) {
    val searchCityState = remember { viewModel.state.searchCityState }
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = searchCityState.text,
        onValueChange = {
            searchCityState.onChanged(it)
            searchCityState.validate()
        },
        label = { Text(text = stringResource(id = R.string.reg_search_city_hint)) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = MaterialTheme.shapes.medium,
        isError = searchCityState.error != null,

        )
    searchCityState.error?.let {
        ErrorField(it)
    }
}

@Composable
fun InterestTagSwitch(viewModel: RegisterViewModel) {
    val switchState by remember { viewModel.state.searchInterestTagSwitch }
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.reg_search_interest_tag_switch_text),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,

            )
        MngSwitch(
            checked = switchState,
            onCheckedChange = { viewModel.state.searchInterestTagSwitch.value = it }
        )
    }

}
