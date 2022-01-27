package com.mangofriends.mangoappnewest.presentation.main.cards

import android.content.res.Resources
import android.util.Log
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
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.common.cardsVisible
import com.mangofriends.mangoappnewest.domain.model.cards.CardDeckEvents
import com.mangofriends.mangoappnewest.domain.model.cards.CardDeckModel
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.main.MainViewModel
import kotlinx.coroutines.CoroutineScope


data class UserCard(
    val index: Int,
    val user: DTOUserProfile,
    val frontLang: String = "English",
    val backLang: String = "English"
)

@ExperimentalCoilApi
@Composable
fun CardView(
    loadProfilesHandler: () -> Unit = {},
    sendLikeHandler: () -> Unit = {},
    viewModel: MainViewModel,
    navController: NavController
) {
    val notifText = stringResource(id = R.string.notification_new_match)
    val context = LocalContext.current
    val data by remember { viewModel.state }
    val dataList = data.toUserCardList()
    var topCardIndex by remember { viewModel.topCardIndex }

    val model =
        CardDeckModel(
            current = topCardIndex,
            dataSource = dataList,
            visible = cardsVisible,
            screenWidth = Resources.getSystem().displayMetrics.widthPixels,
            screenHeight = Resources.getSystem().displayMetrics.heightPixels
//            screenWidth = 1200,
//            screenHeight = 1600
        )


    val events = CardDeckEvents(
        cardWidth = model.cardWidthPx(),
        cardHeight = model.cardHeightPx(),
        model = model,
        peepHandler = {},
        playHandler = { _, _ ->
        },
        nextHandler = { user, isLike ->
            if (isLike) {
                Log.d("SWIPE", "User like ")
                sendLikeHandler.invoke()

//                viewModel.likeUser(viewModel.currentUser.value.uid, user.uid, onLike = {
//                    viewModel.checkMatch(viewModel.currentUser.value, user, onMatch = {
//                        Toast.makeText(context, notifText, Toast.LENGTH_SHORT).show()
//                    })
//                })
            } else {
                Log.d("SWIPE", "User dislike ")
                if (topCardIndex < dataList.lastIndex) {
                    topCardIndex += 1
                } else {
                    loadProfilesHandler.invoke()
                    topCardIndex = 0
                }
            }


        },
    )
    val coroutineScope = rememberCoroutineScope()

    Column {

        CardDeck(coroutineScope,
            model,
            events,
            leftActionHandler = {
                events.cardSwipe.animateToTarget(
                    coroutineScope,
                    CardSwipeState.SWIPED_LEFT
                ) { _, _ ->
                    if (topCardIndex < dataList.lastIndex) {
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
                    sendLikeHandler.invoke()
                }
            },
            infoActionHandler = {
                navController.currentBackStackEntry!!.arguments!!.putParcelable(
                    "profile",
                    it
                )


                navController.navigate(Screen.DetailsScreen.route)
            })
    }
}

private const val TOP_CARD_INDEX = 0
private const val TOP_Z_INDEX = 100f

@ExperimentalCoilApi
@Composable
fun CardDeck(
    coroutineScope: CoroutineScope,
    model: CardDeckModel,
    events: CardDeckEvents,
    infoActionHandler: (cardData: DTOUserProfile) -> Unit = {},
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


            Box(modifier = cardModifier) {
//                val painter = rememberImagePainter(data = cardData.profile_image_urls[0].url)
                Image(
                    painter = rememberImagePainter(data = cardData.profile_image_urls[0].url),
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
                        CardsBottomBar(
                            index = card.index,
                            data = cardData,
                            infoActionHandler = {
                                infoActionHandler.invoke(cardData)
                            },
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
            events.cardSwipe.backToInitialState(coroutineScope)
        }
    }
}