package com.mangofriends.mangoappnewest.presentation.main.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.common.cornerRadiusBig
import com.mangofriends.mangoappnewest.common.normalElevation
import com.mangofriends.mangoappnewest.common.normalSpace
import com.mangofriends.mangoappnewest.common.smallSpace
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.presentation.ui.components.NiceButton
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightGreen
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink

enum class CardFlipState {
    FRONT_FACE, FLIP_BACK, BACK_FACE, FLIP_FRONT
}

enum class CardSwipeState {
    INITIAL, SWIPED_LEFT, SWIPED_RIGHT, DRAGGING
}

//@Preview
//@Composable
//fun TestStudyCardFrontView() {
//    StudyCardView(
//        backgroundColor = primaryColor,
//        side = CardFlipState.FRONT_FACE,
//        modifier = Modifier.size(cardWidth, cardHeight),
//        content = { frontSideColor ->
//            StudyCardsContent(
//                LOREM_IPSUM_FRONT,
//            )
//        },
//        bottomBar = { frontSideColor ->
//            StudyCardsBottomBar(
//                0, 1, CardFlipState.FRONT_FACE, frontSideColor,
//                leftActionHandler = { },
//                rightActionHandler = { }
//            )
//        }
//    )
//}

@Composable
fun StudyCardView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadiusBig),
        elevation = normalElevation,
        content = {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {},
                bottomBar = { bottomBar() },
                content = { content() }
            )
        }
    )
}

@ExperimentalCoilApi
@Composable
fun StudyCardsContent(data: UserProfile) {


    val painter = rememberImagePainter(data = data.profile_image_urls[0].url)
    Image(
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize(),

        )


}

@Composable
fun StudyCardsBottomBar(
    index: Int,
    data: UserProfile,
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
        Text(
            text = data.name + " " + data.age,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(normalSpace),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))
        NiceButton(
            icon = Icons.Filled.Favorite,
            backgroundColor = LightGreen,
            onClick = { rightActionHandler.invoke() }
        )
    }
}
