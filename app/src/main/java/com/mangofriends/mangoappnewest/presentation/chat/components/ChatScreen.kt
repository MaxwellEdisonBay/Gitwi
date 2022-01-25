package com.mangofriends.mangoappnewest.presentation.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBMatch
import com.mangofriends.mangoappnewest.presentation.chat.ChatViewModel
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink

@Composable
fun ChatScreen(
    match: FBMatch, myPhoto: String, navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val message: String by viewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by viewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )
    val isLoading by remember {
        viewModel.isLoading
    }
//    Log.d("CHAT SCREEN", "send to UID = ${match.uid}")
//    Log.d("CHAT SCREEN", "myPhoto = ${myPhoto}")
//    Log.d("CHAT SCREEN", "matchPhoto = ${match.photo}")


    if (isLoading) {
        viewModel.match.value = match
        viewModel.isLoading.value = false
        viewModel.getMessages(match.uid, viewModel.uid)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(LightPink)
                    .padding(16.dp)
            ) {
                Text(
                    text = match.name,
                    style = MaterialTheme.typography.h1,
                    color = Color.White,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 0.85f, fill = true)
                    .padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(messages) { message ->
                    val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean

                    SingleMessage(
                        match = match,
                        message = message,
                        myPhoto = myPhoto,
                        isCurrentUser = isCurrentUser
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(LightPink)
                    .weight(weight = 0.09f, fill = true)
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        viewModel.updateMessage(it)
                    },
//                    label = {
//                        Text(modifier = Modifier,
//                            text ="Type Your Message"
//                        )
//                    },

                    maxLines = 1,
                    modifier = Modifier


                        .padding(horizontal = 15.dp, vertical = 8.dp)

                        .fillMaxWidth()
//                        .shadow(0.dp)
//                        .border(BorderStroke(0.dp,Color.Transparent))

                        .background(Color.White, RoundedCornerShape(5.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Gray,
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        trailingIconColor = Color.Gray,
                        disabledTrailingIconColor = LightPink
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.addMessage(viewModel.uid, match)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Button"
                            )
                        }
                    }
                )
            }

        }
    }
}


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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
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
//                        .()
                        .wrapContentWidth()
                        .padding(16.dp),
                    color = if (!isCurrentUser) MaterialTheme.colors.primary else Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))


            Image(
                painter = rememberImagePainter(data = myPhoto),
                modifier = Modifier
                    .requiredSize(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                contentDescription = "Message Photo",
            )
        }

    }

}
