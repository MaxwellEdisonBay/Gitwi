package com.mangofriends.mangoappnewest.presentation.main.cards

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.domain.model.cards.CardDeckEvents
import com.mangofriends.mangoappnewest.domain.model.cards.CardDeckModel
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.presentation.main.MainViewModel
import kotlinx.coroutines.CoroutineScope


data class UserCard(
    val index: Int,
    val user: UserProfile,
    val frontLang: String = "English",
    val backLang: String = "English"
)

@ExperimentalCoilApi
@Composable
fun TestStudyCardView(
    loadProfilesHandler: () -> Unit = {},
    sendLikeHandler: () -> Unit = {},
    viewModel: MainViewModel
) {
    val notifText = stringResource(id = R.string.notification_new_match)
    val context = LocalContext.current
    val data by remember { viewModel.data }
    var topCardIndex by remember { viewModel.topCardIndex }
    val model = CardDeckModel(
        current = topCardIndex,
        dataSource = data,
        visible = 2,
        screenWidth = 1200,
        screenHeight = 1600
    )
    val events = CardDeckEvents(
        cardWidth = model.cardWidthPx(),
        cardHeight = model.cardHeightPx(),
        model = model,
        peepHandler = {},
        playHandler = { _, _ ->
        },
        nextHandler = { uid, isLike ->
            if (topCardIndex < data.lastIndex) {
                topCardIndex += 1
            } else {
                loadProfilesHandler.invoke()
                topCardIndex = 0
            }
            if (isLike) {
                Log.d("REACTION", "User like ")

                viewModel.likeUser(viewModel.uid, uid, onLike = {
                    viewModel.checkMatch(viewModel.uid, uid, onMatch = {
                        Toast.makeText(context, notifText, Toast.LENGTH_SHORT).show()
                    })
                })
            } else {
                Log.d("REACTION", "User dislike ")

            }
        },
    )
    val coroutineScope = rememberCoroutineScope()

    Column {

        StudyCardDeck(coroutineScope,
            model,
            events,
            leftActionHandler = {
                events.cardSwipe.animateToTarget(
                    coroutineScope,
                    CardSwipeState.SWIPED_LEFT
                ) { _, _ ->
                    if (topCardIndex < data.lastIndex) {
                        topCardIndex += 1
                    } else {
                        loadProfilesHandler.invoke()
                        topCardIndex = 0
                    }
                }
            },
            rightActionHandler = {
                events.cardSwipe.animateToTarget(
                    coroutineScope,
                    CardSwipeState.SWIPED_RIGHT
                ) { _, _ ->
                    if (topCardIndex < data.lastIndex) {
                        topCardIndex += 1
                    } else {
                        loadProfilesHandler.invoke()
                        topCardIndex = 0
                    }
                    sendLikeHandler.invoke()
                }
            })
    }
}

private const val TOP_CARD_INDEX = 0
private const val TOP_Z_INDEX = 100f

@ExperimentalCoilApi
@Composable
fun StudyCardDeck(
    coroutineScope: CoroutineScope,
    model: CardDeckModel,
    events: CardDeckEvents,
    leftActionHandler: () -> Unit = {},
    rightActionHandler: () -> Unit = {},
) {

    events.apply {
        flipCard.Init()
        cardsInDeck.Init()
        cardSwipe.Init()
    }
    Box(Modifier.fillMaxSize()) {

        repeat(model.visibleCards) { visibleIndex ->
            val card = model.cardVisible(visibleIndex)
            val cardData = card.user

            val cardZIndex = TOP_Z_INDEX - visibleIndex
            val cardModifier = events
                .makeCardModifier(
                    coroutineScope,
                    TOP_CARD_INDEX,
                    visibleIndex
                )
                .align(Alignment.Center)
                .zIndex(cardZIndex)
                .fillMaxSize()


            val painter = rememberImagePainter(data = cardData.profile_image_urls[0].url)
            Box(modifier = cardModifier) {
                Image(
                    painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    if (visibleIndex == TOP_CARD_INDEX) {
                        StudyCardsBottomBar(
                            index = card.index,
                            data = cardData,

                            leftActionHandler = {
                                Log.d("ACTION", "Left action ${cardData.name}")
                                leftActionHandler.invoke()

                            },
                            rightActionHandler = {
                                Log.d("ACTION", "Right action ${cardData.name}")
                                rightActionHandler.invoke()
                            }
                        )

                    }
                }

            }


//                StudyCardView(
//                modifier = cardModifier,
//                content = {
//                        StudyCardsContent(
//                            cardData
//                        )
//                },
//                bottomBar = { frontSideColor ->
//                    if (visibleIndex == TOP_CARD_INDEX) {
//                        StudyCardsBottomBar(
//                            index = card.index,
//                            data = cardData,
//
//                            leftActionHandler = {
//                                Log.d("ACTION", "Left action ${cardData.name}")
//                                leftActionHandler.invoke()
//
//                            },
//                            rightActionHandler = {
//                                Log.d("ACTION", "Right action ${cardData.name}")
//                                rightActionHandler.invoke()
//                            }
//                        )
//
//                    }
//                }
//
//            )
            events.cardSwipe.backToInitialState(coroutineScope)
        }
    }
}