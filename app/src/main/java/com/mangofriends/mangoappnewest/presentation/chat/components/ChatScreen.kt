package com.mangofriends.mangoappnewest.presentation.chat.components

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBMatch
import com.mangofriends.mangoappnewest.presentation.chat.ChatViewModel
import com.mangofriends.mangoappnewest.presentation.components.StringUtils.Companion.ignoreBlank
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun ChatScreen(
    match: FBMatch, myPhoto: String, navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val relocation = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    val messageHint = stringResource(id = R.string.send_message_hint)
    val message: String by viewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by viewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )
    val isLoading by remember { viewModel.isLoading }
    val activity = LocalContext.current as Activity
    val window = activity.window
    var shouldResize = false // false will resize
    shouldResize = messages.size > 1
//    Log.d("IS TYPING", isTyping.toString())
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        window.setDecorFitsSystemWindows(shouldResize)
////        shouldResize = shouldResize.not()
//    } else {
//        if (shouldResize.not()) {
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//        } else {
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
//        }
//    }
//    Log.d("CHAT SCREEN", "send to UID = ${match.uid}")
//    Log.d("CHAT SCREEN", "myPhoto = ${myPhoto}")
//    Log.d("CHAT SCREEN", "matchPhoto = ${match.photo}")


    if (isLoading) {
        viewModel.match.value = match
        viewModel.isLoading.value = false
        viewModel.getMessages(match.uid, viewModel.uid)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightPink)
                    .padding(16.dp)
                    .weight(weight = 0.8f, fill = true)
//                    .bringIntoViewRequester(relocation)
//                    .onFocusEvent {
//                        if (it.isFocused) scope.launch { delay(300); relocation.bringIntoView() }
//                    }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = match.name,
                    style = MaterialTheme.typography.h1,
                    color = Color.White,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .weight(weight = 8f, fill = false)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter)
//                    .bringIntoViewRequester(relocation)
//                    .onFocusEvent {
//                        if (it.isFocused) scope.launch { delay(300); relocation.bringIntoView() }
//                    }
                    ,
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    reverseLayout = true
                ) {
                    itemsIndexed(messages) { index, message ->
                        val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean
                        if (index == 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        SingleMessage(
                            match = match,
                            message = message,
                            myPhoto = myPhoto,
                            isCurrentUser = isCurrentUser
                        )
                        if (index == messages.lastIndex) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .background(LightPink)
                    .weight(weight = 1f, fill = true)
                    .bringIntoViewRequester(relocation)
                    .onFocusEvent {
                        if (it.isFocused) scope.launch {
                            delay(200);
                            relocation.bringIntoView()
                        }
                    }

            ) {
                val color = if (message.isEmpty()) Color.Gray else Color.White

                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        viewModel.updateMessage(it.ignoreBlank())
                    },
                    label = { Text(text = messageHint, color = color) },
//                    placeholder = { Text(text = messageHint, modifier = Modifier.fillMaxSize().align(
//                        Alignment.CenterStart)) },
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 8.dp)
                        .fillMaxSize()
                        .background(Color.White, RoundedCornerShape(5.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Gray,
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        trailingIconColor = Color.Gray,
                        disabledTrailingIconColor = LightPink,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = false,
                    trailingIcon = {
                        if (message.isNotEmpty())
                            IconButton(
                                onClick = {
                                    viewModel.addMessage(viewModel.uid, match)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send Button"
                                )
                            }
                    }
                )
//                Box(modifier = Modifier.fillMaxSize()){
//                    if (message.isEmpty())
//                    Text(
//                        text = messageHint, modifier = Modifier
////                        .fillMaxSize()
//                            .align(
//                                Alignment.CenterStart
//                            ).padding(start = 16.dp), color = Color.Black
//                    )
//                }


            }

        }
    }
}


@ExperimentalCoilApi
@Composable
fun SingleMessage(
    message: Map<String, Any>,
    match: FBMatch,
    myPhoto: String,
    isCurrentUser: Boolean
) {
    if (!isCurrentUser)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberImagePainter(data = match.photo),
                modifier = Modifier
                    .requiredSize(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                contentDescription = "Message Photo"
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (isCurrentUser) MaterialTheme.colors.primary else Color.White,
            ) {
                Text(
                    text = message[Constants.MESSAGE].toString(),
                    textAlign =
                    if (isCurrentUser)
                        TextAlign.End
                    else
                        TextAlign.Start,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(16.dp),
                    color = if (!isCurrentUser) MaterialTheme.colors.primary else Color.White
                )
            }
        }
    else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(8f, true),
                horizontalArrangement = Arrangement.End
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = if (isCurrentUser) MaterialTheme.colors.primary else Color.White,
                ) {
                    Text(
                        text = message[Constants.MESSAGE].toString(),
                        textAlign =
                        if (isCurrentUser)
                            TextAlign.End
                        else
                            TextAlign.Start,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(16.dp),
                        color = if (!isCurrentUser) MaterialTheme.colors.primary else Color.White
                    )
                }

            }

            Spacer(modifier = Modifier.width(8.dp))


            Image(
                painter = rememberImagePainter(data = myPhoto),
                modifier = Modifier
                    .requiredSize(40.dp)
                    .clip(CircleShape)
                    .weight(1f, false),
                contentScale = ContentScale.Crop,
                contentDescription = "Message Photo",
            )
        }

    }

}
