package com.mangofriends.mangoappnewest.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

object Constants {
    const val PARAM_UID = "uid"

    const val CODE_SUCCESS = 200
    const val CODE_ERROR = -1
    const val READ_TIMEOUT_SECONDS = 60
    const val CONNECTION_TIMEOUT_SECONDS = 60

    const val BACK_PRESS_DELAY = 2000
    const val START_TIME = -2000L

    const val MIN_AGE = 18f
    const val MAX_AGE = 50f

    const val EMPTY_LAST_MESSAGE = "Say hi to "
    const val EMPTY_LAST_MESSAGE_TIME = -1L

    const val DATE_FORMATTING = "HH:mm:ss\tdd.MM.yyyy"
    const val LATEST_MESSAGE_LENGTH = 20

    const val MESSAGES = "messages"
    const val MESSAGE = "message"
    const val MATCH_NAME = "match_name"
    const val MATCH_PHOTO = "match_photo"
    const val SENT_BY = "sent_by"
    const val SENT_TO = "sent_to"
    const val FROM_TO_UID = "from_to"
    const val TIMESTAMP = "timestamp"
    const val IS_CURRENT_USER = "is_current_user"

    const val TAG = "app-chat"

    val DETAIL_ROWS_ICONS = listOf(
        Icons.Filled.Face,
        Icons.Filled.Living,
        Icons.Filled.LocationCity,
        Icons.Filled.Work,
        Icons.Filled.ShortText


    )


}