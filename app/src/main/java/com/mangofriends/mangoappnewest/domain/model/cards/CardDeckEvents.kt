package com.mangofriends.mangoappnewest.domain.model.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.mangofriends.mangoappnewest.common.paddingOffset
import com.mangofriends.mangoappnewest.presentation.main.animations.CardSwipeAnimation
import com.mangofriends.mangoappnewest.presentation.main.animations.CardsInDeckAnimation
import com.mangofriends.mangoappnewest.presentation.main.animations.FlipCardAnimation
import com.mangofriends.mangoappnewest.presentation.main.cards.CardSwipeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class CardDeckEvents(
    val cardWidth: Float,
    val cardHeight: Float,
    val model: CardDeckModel,
    val peepHandler: () -> Unit,
    val playHandler: (String, String) -> Unit,
    val nextHandler: (String, Boolean) -> Unit,
    val actionCallback: (String) -> Unit = {}
) {
    val cardSwipe: CardSwipeAnimation = CardSwipeAnimation(
        model = model,
        cardWidth = cardWidth,
        cardHeight = cardHeight
    )
    val flipCard = FlipCardAnimation(peepHandler)
    val cardsInDeck = CardsInDeckAnimation(paddingOffset, model.count)

    @SuppressLint("ModifierFactoryExtensionFunction")
    fun makeCardModifier(
        coroutineScope: CoroutineScope,
        topCardIndex: Int,
        idx: Int
    ): Modifier {
        return if (idx > topCardIndex) {
//            Modifier
//                .scale(cardsInDeck.scaleX(idx), 1f)
//                .offset { cardsInDeck.offset(idx) }
            Modifier
        } else {
            Modifier
//                .scale(flipCard.scaleX(), flipCard.scaleY())
                .offset { cardSwipe.toIntOffset() }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            cardSwipe.animateToTarget(
                                coroutineScope,
                                CardSwipeState.DRAGGING
                            ) { nextFlag, isLike ->
                                if (nextFlag) {

                                    coroutineScope.launch {
                                        flipCard.backToInitState()
                                    }
                                    if (isLike)
                                        nextHandler(model.dataSource[topCardIndex].user.uid, true)
                                    else
                                        nextHandler(model.dataSource[topCardIndex].user.uid, false)
                                }

                            }
                            cardsInDeck.backToInitState()

                        },
                        onDrag = { change, _ ->
                            cardSwipe.draggingCard(coroutineScope, change) {
//                                cardsInDeck.pushBackToTheFront()
                            }
                        }
                    )
                }
        }
    }
}
