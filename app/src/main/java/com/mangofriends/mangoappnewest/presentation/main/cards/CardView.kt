package com.mangofriends.mangoappnewest.presentation.main.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mangofriends.mangoappnewest.common.smallSpace
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.presentation.ui.components.NiceButton
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightGreen
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink

enum class CardFlipState {
    FRONT_FACE, FLIP_BACK, BACK_FACE, FLIP_FRONT
}

enum class CardSwipeState {
    INITIAL, SWIPED_LEFT, SWIPED_RIGHT, DRAGGING
}

@Composable
fun CardsBottomBar(
    index: Int,
    data: DTOUserProfile,
    infoActionHandler: () -> Unit = {},
    leftActionHandler: () -> Unit = {},
    rightActionHandler: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(smallSpace)
            .fillMaxWidth()
    ) {

        NiceButton(
            icon = Icons.Filled.Close,
            backgroundColor = LightPink,
            onClick = { leftActionHandler.invoke() }
        )
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = data.name + " " + data.age,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
//                    .padding(normalSpace),
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Filled.Info, "",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { infoActionHandler.invoke() }
            )

        }

        Spacer(Modifier.weight(1f))
        NiceButton(
            icon = Icons.Filled.Favorite,
            backgroundColor = LightGreen,
            onClick = { rightActionHandler.invoke() }
        )
    }
}
