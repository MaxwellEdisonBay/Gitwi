package com.mangofriends.mangoappnewest.presentation.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBMatch
import com.mangofriends.mangoappnewest.presentation.components.StringUtils
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink
import java.sql.Date
import java.text.SimpleDateFormat

@ExperimentalCoilApi
@Composable
fun ProfilesListItem(
    profile: FBMatch,
    onItemClick: (FBMatch) -> Unit
) {
    val imagePainter = rememberImagePainter(data = profile.photo)
    val sdf = SimpleDateFormat(Constants.DATE_FORMATTING)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onItemClick(profile) },
//        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "User Image",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            Text(
                text = profile.name,
                style = MaterialTheme.typography.h3,
                color = Pink,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.Start)

            )
            Text(
                text = StringUtils.limitString(
                    profile.last_message,
                    Constants.LATEST_MESSAGE_LENGTH
                ),
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = sdf.format(Date(profile.last_message_time)),
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.End)
            )
        }

//        Spacer(modifier = Modifier.height(15.dp))
//        FlowRow(
//            mainAxisSpacing = 10.dp,
//            crossAxisSpacing = 10.dp,
//            modifier = Modifier.fillMaxWidth()
//
//        ) {
//            profile.tags.forEach {
//                ProfileTag(tag = it)
//            }
//        }

    }
}