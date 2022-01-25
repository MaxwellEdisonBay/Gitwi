package com.mangofriends.mangoappnewest.domain.model.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import com.mangofriends.mangoappnewest.common.cardHeight
import com.mangofriends.mangoappnewest.common.cardWidth
import com.mangofriends.mangoappnewest.presentation.main.cards.UserCard

data class CardDeckModel(
    var current: Int,
    val dataSource: List<UserCard>,
    val visible: Int = 5,
    val screenWidth: Int,
    val screenHeight: Int
) {
    val count = dataSource.size
    val visibleCards: Int = StrictMath.min(visible, dataSource.size - current)

    fun cardVisible(visibleIndex: Int) = dataSource[dataSourceIndex(visibleIndex)]

    private fun dataSourceIndex(visibleIndex: Int): Int {
        return current + visibleIndex
    }


    @Composable
    fun cardWidthPx(): Float {
        return with(LocalDensity.current) { cardWidth.toPx() }
    }

    @Composable
    fun cardHeightPx(): Float {
        return with(LocalDensity.current) { cardHeight.toPx() }
    }
}