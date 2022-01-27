package com.mangofriends.mangoappnewest.presentation.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBMatch
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {
    val isLoading = mutableStateOf(true)
    val match = mutableStateOf(FBMatch())
    val uid = firebaseRepository.getUid()

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages


    fun updateMessage(message: String) {
        _message.value = message
    }


    fun addMessage(fromUid: String, match: FBMatch) {
        val message: String = _message.value ?: throw IllegalArgumentException("message empty")
        if (message.isNotEmpty()) {
            Firebase.firestore.collection(Constants.MESSAGES).document().set(
                hashMapOf(
                    Constants.MESSAGE to message.trimIndent().trim(),
                    Constants.FROM_TO_UID to "$fromUid ${match.uid}",
                    Constants.TIMESTAMP to System.currentTimeMillis(),
                    Constants.MATCH_NAME to match.name,
                    Constants.MATCH_PHOTO to match.photo,

                    )
            ).addOnSuccessListener {
                _message.value = ""
            }
            val database = FirebaseDatabase.getInstance().getReference("/users")
            database.child("$fromUid/matches/${match.uid}/last_message").setValue(message)
            database.child("$fromUid/matches/${match.uid}/last_message_time")
                .setValue(System.currentTimeMillis())
            database.child("${match.uid}/matches/${fromUid}/last_message").setValue(message)
            database.child("${match.uid}/matches/${fromUid}/last_message_time")
                .setValue(System.currentTimeMillis())


        }
    }

    fun getMessages(toUid: String, fromUid: String) {
        Firebase.firestore.collection(Constants.MESSAGES)
            .whereIn(Constants.FROM_TO_UID, listOf("$fromUid $toUid", "$toUid $fromUid"))
            .orderBy(Constants.TIMESTAMP)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(Constants.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val list = emptyList<Map<String, Any>>().toMutableList()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        data[Constants.IS_CURRENT_USER] =
                            "$fromUid $toUid" == data[Constants.FROM_TO_UID].toString()

                        list.add(data)
                    }
                }

                updateMessages(list)
            }

    }

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }
}