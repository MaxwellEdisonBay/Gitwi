package com.mangofriends.mangoappnewest.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBRequestCall
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.presentation.components.Screen
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception

class FirebaseRepositoryImpl @Inject constructor(

) : FirebaseRepository {
    override suspend fun registerWithEmailAndPassword(credentials: UserCredentials): FBRequestCall {
        val result = FBRequestCall()
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.status = Constants.CODE_SUCCESS
                }
            }
            .addOnFailureListener { e ->
                result.status = Constants.CODE_ERROR
                result.message = e.message.toString()
            }
        return result
    }

    override suspend fun signInWithEmailAndPassword(credentials: UserCredentials, navController: NavController): FBRequestCall {
        val result = FBRequestCall()
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.status = Constants.CODE_SUCCESS
                    Log.d("DEBUG TAG", "SUCCESS ${auth.currentUser?.uid ?: "null" }")
                    navController.navigate(Screen.MainScreen.withArgs(auth.currentUser?.uid ?: "null"))

                }
            }
            .addOnFailureListener {

                result.status = Constants.CODE_ERROR
                result.message = it.message.toString()
                Log.d("DEBUG TAG", "SUCCESS ${result.message}")
            }
            .await()
        return result
    }


    override fun uploadImagesToFireCloud(uris: List<String>): List<String> {
        return emptyList()
    }

    override fun uploadregistrationDataToFirebase(profile: UserProfile): MutableLiveData<FBRequestCall> {
        val a = MutableLiveData<FBRequestCall>(FBRequestCall())
        return a
    }

    override fun checkIfLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser==null
    }

    override fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
    }

    override fun getUID(): String? {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser?.uid
    }


}