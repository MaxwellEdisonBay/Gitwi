package com.mangofriends.mangoappnewest.presentation.main.animations

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.IntOffset
import com.mangofriends.mangoappnewest.common.animationTime
import com.mangofriends.mangoappnewest.common.paddingOffset
import com.mangofriends.mangoappnewest.domain.model.cards.CardDeckModel
import com.mangofriends.mangoappnewest.presentation.main.cards.CardSwipeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class CardSwipeAnimation(
    val model: CardDeckModel,
    val cardWidth: Float,
    val cardHeight: Float
) {
    private lateinit var cardDragOffset: Animatable<Offset, AnimationVector2D>

    @Composable
    fun Init() {
        cardDragOffset = remember {
            Animatable(
                targetValueByState(CardSwipeState.INITIAL),
                Offset.VectorConverter,
            )
        }
    }

    private fun targetValueByState(state: CardSwipeState): Offset {
        return when (state) {
            CardSwipeState.INITIAL -> {
                Offset(0F, paddingOffset)
            }
            CardSwipeState.SWIPED_LEFT -> {
                Offset(-1 * cardWidth, paddingOffset)
            }
            CardSwipeState.SWIPED_RIGHT -> {
                Offset(model.screenWidth.toFloat() + cardWidth, paddingOffset)
            }
            else -> {
                swipeDirection()
            }
        }
    }

    private val animationSpec: FiniteAnimationSpec<Offset> = tween(
        durationMillis = animationTime,
        easing = FastOutLinearInEasing
    )

    private fun swipeDirection(): Offset {
        val halfW = model.screenWidth / 2f
        val halfH = model.screenHeight / 2f
        val x = when {
            cardDragOffset.value.x > halfW -> model.screenWidth.toFloat()
            cardDragOffset.value.x + cardWidth < halfW -> -cardWidth
            else -> 0f
        }
        val y = when {
            cardDragOffset.value.y > halfH -> model.screenHeight.toFloat()
            cardDragOffset.value.y + cardHeight < halfH -> -cardHeight
            else -> 0f
        }
        return Offset(x, y)
    }

    fun animateToTarget(
        coroutineScope: CoroutineScope,
        state: CardSwipeState,
        finishedCallback: (Boolean, Boolean) -> Unit
    ) {
        coroutineScope.launch {
            val target = targetValueByState(state)
            cardDragOffset.animateTo(
                targetValue = target,
                animationSpec = animationSpec,
                block = {
                    if (value.x == targetValue.x &&
                        value.y == targetValue.y
                    ) {
                        val next = !(targetValue.x == 0f && targetValue.y == 0f)
                        val like = !(targetValue.x <= 0f)
                        finishedCallback(next, like)
                    }
                }
            )
        }
    }

    fun toIntOffset(): IntOffset {
        return IntOffset(
            cardDragOffset.value.x.toInt(),
            cardDragOffset.value.y.toInt()
        )
    }

    fun backToInitialState(coroutineScope: CoroutineScope) {
        snapTo(coroutineScope, targetValueByState(CardSwipeState.INITIAL))
    }

    private fun snapTo(coroutineScope: CoroutineScope, target: Offset) {
        coroutineScope.launch {
            cardDragOffset.snapTo(target)
        }
    }

    fun draggingCard(
        coroutineScope: CoroutineScope,
        change: PointerInputChange,
        callBack: () -> Unit
    ) {
        if (change.pressed) {
            val original =
                Offset(
                    cardDragOffset.value.x,
                    cardDragOffset.value.y
                )
            val summed = original + change.positionChange()
            val newValue = Offset(
                x = summed.x,
                y = summed.y
            )
            change.consumePositionChange()
            snapTo(coroutineScope, Offset(newValue.x, newValue.y))
            callBack()
        }
    }
}