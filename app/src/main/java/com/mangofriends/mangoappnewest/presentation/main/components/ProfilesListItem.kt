package com.mangofriends.mangoappnewest.presentation.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile

@Composable
fun ProfilesListItem(
    profile: UserProfile,
    onItemClick: (UserProfile) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(profile) }
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "${profile.name}. ${profile.lastname} - ${profile.age}",
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.Start)

        )
        Text(
            text = profile.bio,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(15.dp))
        FlowRow(
            mainAxisSpacing = 10.dp,
            crossAxisSpacing = 10.dp,
            modifier = Modifier.fillMaxWidth()

        ) {
            profile.tags.forEach {
                ProfileTag(tag = it)
            }
        }
    }
}