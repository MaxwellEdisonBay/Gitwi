package com.mangofriends.mangoappnewest.data.repository

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.firebase_models.*
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.main.MainViewModel
import com.mangofriends.mangoappnewest.presentation.register.RegisterViewModel
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(

) : FirebaseRepository {

    override suspend fun registerWithEmailAndPassword(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit
    ): FBRequestCall {
        val result = FBRequestCall()
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(
            viewModel.state.emailState.text,
            viewModel.state.password1State.text
        ).addOnSuccessListener {
            result.status = Constants.CODE_SUCCESS
            viewModel.state.uidState.value = auth.currentUser!!.uid
            uploadImagesToFireCloud(viewModel, navController, onError)
            Log.d("REGISTER AUTH", "SUCCESS UID ${viewModel.state.uidState.value}")
        }.addOnFailureListener { e ->
            result.status = Constants.CODE_ERROR
            result.message = e.message.toString()
            onError.invoke(e.message.toString())
        }.await()


        return result
    }

    override suspend fun signInWithEmailAndPassword(
        credentials: UserCredentials,
        navController: NavController,
        onError: (String) -> Unit
    ): FBRequestCall {
        val result = FBRequestCall()
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.status = Constants.CODE_SUCCESS
                    Log.d("DEBUG TAG", "SUCCESS ${auth.currentUser?.uid ?: "null"}")
                    navController.navigate(
                        Screen.MainScreen.withArgs(
                            auth.currentUser?.uid ?: "null"
                        )
                    )
                }
            }
            .addOnFailureListener {

                result.status = Constants.CODE_ERROR
                result.message = it.message.toString()
                onError.invoke(result.message)
                Log.d("DEBUG TAG", "ERROR ${it.message}")
            }
            .await()
        return result
    }


    override fun uploadImagesToFireCloud(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit
    ): FBRequestCall {
        val result = FBRequestCall()


        val storageRef = Firebase.storage.reference
        val ref = storageRef.child("images/profile_photos")
        val uploadTask = ref.putFile(Uri.parse(viewModel.state.imageUriState.value.toString()))

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                result.status = Constants.CODE_SUCCESS
                Log.d("DOWNLOAD", downloadUri.toString())
                val imageUuid = UUID.randomUUID().toString()
                viewModel.state.urlState.add(
                    FBProfileImageUrl(
                        imageUuid,
                        0,
                        downloadUri.toString()
                    )
                )
                viewModel.state.tagState.add(FBTag(imageUuid, "SAMPLE_TAG", 0))
                uploadRegistrationDataToFirebase(viewModel, navController, onError)
            } else {
                result.status = Constants.CODE_ERROR
                result.message = task.exception!!.message.toString()
                onError.invoke(result.message)
                Log.d("DOWNLOAD", "ERROR ${task.exception!!.message.toString()}")
            }

        }
        return result
    }

    override fun uploadRegistrationDataToFirebase(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit
    ): FBRequestCall {
        val result = FBRequestCall()

        val database = FirebaseDatabase.getInstance().reference
        val uid = viewModel.state.uidState.value

        database.child("users").child(uid)
            .setValue(
                viewModel.state.getProfile()
            )
            .addOnSuccessListener {
                Log.d("USER INFO", "SUCCESS #1")
                result.status = Constants.CODE_SUCCESS
                val imageUuid = viewModel.state.urlState.first().img_id
                database.child("users").child(uid).child("profile_image_urls").child(imageUuid)
                    .setValue(
                        viewModel.state.urlState.first()
                    )

                    .addOnSuccessListener {
                        Log.d("USER INFO", "SUCCESS #3")
                        database.child("users").child(uid).child("tags")
                            .child(imageUuid)
                            .setValue(
                                viewModel.state.tagState.first()
                            ).addOnSuccessListener {
                                result.status = Constants.CODE_SUCCESS
                                navController.navigate(Screen.MainScreen.route + "/Maxwell") {
                                    popUpTo(0)
                                }
                            }
                    }
            }.addOnFailureListener {
                result.status = Constants.CODE_ERROR
                result.message = it.message.toString()
                onError.invoke(result.message)
                Log.d("USER INFO", "ERROR #2 ${result.message}")
            }.addOnFailureListener {
                result.status = Constants.CODE_ERROR
                result.message = it.message.toString()
                onError.invoke(result.message)
                Log.d("USER INFO", "ERROR #1 ${result.message}")
            }

        return result
    }

    override fun checkIfLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser == null
    }

    override fun getUid(): String {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser?.uid ?: ""
    }

    override fun likeUser(myUid: String, uid: String, onLike: () -> Unit): FBRequestCall {
        val result = FBRequestCall()
        val database = Firebase.database.reference
        val liked = FBLike(uid, System.currentTimeMillis())
        val likedMe = FBLike(myUid, System.currentTimeMillis())

        database.child("users").child(uid).child("liked-me").child(myUid).setValue(likedMe)
            .addOnSuccessListener {
                database.child("users").child(myUid).child("liked").child(uid).setValue(liked)
                    .addOnSuccessListener {
                        result.status = Constants.CODE_SUCCESS
                        Log.d("FB DATABASE", "Liked successfully")
                        onLike.invoke()
                    }
                    .addOnFailureListener {
                        result.status = Constants.CODE_ERROR
                        result.message = it.message.toString()
                        Log.d("FB DATABASE", "ERROR ${it.message}")
                    }.addOnFailureListener {
                        result.status = Constants.CODE_ERROR
                        result.message = it.message.toString()
                        Log.d("FB DATABASE", "ERROR ${it.message}")
                    }
            }

        return FBRequestCall()
    }

    override fun checkMatch(myUid: String, uid: String, onMatch: () -> Unit): FBRequestCall {
        val result = FBRequestCall()
        val database = Firebase.database.reference
        database.child("users").child(uid).child("liked").get().addOnSuccessListener { iter ->
            val likes = iter.children
            likes.forEach {
                val value = it.getValue(FBLike::class.java)
                Log.i("firebase", "Got value ${value}\n")
                if (value!!.uid == myUid) {
                    Log.d("MATCH", "Here is a match with ${value.uid}\n")
                    val liked = FBLike(uid, System.currentTimeMillis())
                    val likedMe = FBLike(myUid, System.currentTimeMillis())
                    database.child("users").child(uid).child("matches").child(myUid)
                        .setValue(likedMe).addOnSuccessListener {
                            database.child("users").child(myUid).child("matches").child(uid)
                                .setValue(liked).addOnSuccessListener {
                                    Log.i("MATCH", "Match Success\n")
                                    onMatch.invoke()
                                    result.status = Constants.CODE_SUCCESS
                                }
                        }
                } else {
                    result.status = Constants.CODE_SUCCESS
                    Log.i("MATCH", "No match Success\n")
                }

            }


        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            result.status = Constants.CODE_ERROR
            result.message = it.message.toString()
        }



        return result
    }

    override fun signOut(navController: NavController) {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        navController.navigate(Screen.LoginScreen.route) {
            popUpTo(0)
        }
    }

    override fun loadMatches(viewModel: MainViewModel) {
        val database = Firebase.database
        val uid = viewModel.uid
        val reflatitude = database.getReference("/user/${uid}/matches")
        reflatitude.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("LOAD MATCHES", dataSnapshot.toString())

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("LOAD MATCHES", "Failed to read value.", error.toException())
            }
        })
    }

}