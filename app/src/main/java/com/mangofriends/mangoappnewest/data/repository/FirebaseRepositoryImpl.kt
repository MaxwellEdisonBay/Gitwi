package com.mangofriends.mangoappnewest.data.repository

import android.app.Application
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
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.*
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.presentation.components.ImageUtils
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
        }.addOnFailureListener {
            viewModel.state.error.value = it.message ?: ""
            viewModel.state.isLoading.value = false
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
                    Log.d("LOGIN", "SUCCESS ${auth.currentUser?.uid ?: "null"}")
                    navController.navigate(
                        Screen.MainScreen.route
                    ) {
                        popUpTo(0)
                    }
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
        val uri = viewModel.state.imageUriState.value.uri
        val storageRef = Firebase.storage.reference
        val imageUUID = UUID.randomUUID()
        val ref = storageRef.child("images/$imageUUID")
        val contentResolver = viewModel.getApplication<Application>().contentResolver
        val compressedBytes = ImageUtils.getCompressedBytes(contentResolver, uri!!)
        val uploadTask = ref.putBytes(compressedBytes)

        uploadTask.continueWithTask { task ->
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
//                Log.d("DOWNLOAD", "ERROR ${task.exception!!.message.toString()}")
            }

        }
            .addOnFailureListener {
                viewModel.state.error.value = it.message ?: ""
                viewModel.state.isLoading.value = false
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
                Log.d("USER INFO", "SUCCESS #1 - User Profile")
                result.status = Constants.CODE_SUCCESS
                val imageUuid = viewModel.state.urlState.first().img_id
                database.child("users").child(uid).child("profile_image_urls").child(imageUuid)
                    .setValue(
                        viewModel.state.urlState.first()
                    )

                    .addOnSuccessListener {
                        Log.d("USER INFO", "SUCCESS #2 - User profile image urls")
                        database.child("users").child(uid).child("tags")
                            .child(imageUuid)
                            .setValue(
                                viewModel.state.tagState.first()
                            ).addOnSuccessListener {
                                Log.d("USER INFO", "SUCCESS #2 - User profile interest tags")
                                result.status = Constants.CODE_SUCCESS
                                viewModel.state.isLoading.value = false
                                navController.navigate(Screen.MainScreen.route) {
                                    popUpTo(0)
                                }
                            }.addOnFailureListener {
                                viewModel.state.error.value = it.message ?: ""
                                viewModel.state.isLoading.value = false
                            }
                    }.addOnFailureListener {
                        viewModel.state.error.value = it.message ?: ""
                        viewModel.state.isLoading.value = false
                    }

            }.addOnFailureListener {
                viewModel.state.error.value = it.message ?: ""
                viewModel.state.isLoading.value = false
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

        database.child("users").child(uid).child("liked_me").child(myUid).setValue(likedMe)
            .addOnSuccessListener {
                database.child("users").child(myUid).child("liked").child(uid).setValue(liked)
                    .addOnSuccessListener {
                        result.status = Constants.CODE_SUCCESS
                        Log.d("FB DATABASE", "I am $myUid, Liked $uid successfully")
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

    override fun checkMatch(
        currentUser: FBUserProfile,
        user: DTOUserProfile,
        onMatch: (Boolean) -> Unit
    ): FBRequestCall {
        val result = FBRequestCall()
        val database = Firebase.database.getReference("/users")
        val likedMeList = currentUser.liked_me.keys.toList()
        Log.d("CHECK MATCH", "Current user is ${user.uid}")
        Log.d("DEBUG_MATCH", currentUser.liked_me.keys.toList().toString())
        if (likedMeList.isNotEmpty()) {
            var flag = false
            likedMeList.forEach { iter ->
                Log.d(
                    "CHECK MATCH",
                    "Checking match with liked-me element $iter (I liked ${user.uid})"
                )
                if (iter == user.uid) {
                    flag = true
                    Log.d("MATCH", "Here is a match with ${iter}\n")
                    val liked = FBMatch(
                        user.uid,
                        System.currentTimeMillis(),
                        user.profile_image_urls[0].url,
                        user.name,
                        Constants.EMPTY_LAST_MESSAGE + "${user.name}!",
                        System.currentTimeMillis()
                    )
                    val likedMe = FBMatch(
                        currentUser.uid,
                        System.currentTimeMillis(),
                        currentUser.profile_image_urls.values.toList()[0].url,
                        currentUser.name,
                        Constants.EMPTY_LAST_MESSAGE + "${currentUser.name}!",
                        System.currentTimeMillis()
                    )
                    database.child(user.uid).child("matches").child(currentUser.uid)
                        .setValue(likedMe).addOnSuccessListener {
                            database.child(currentUser.uid).child("matches").child(user.uid)
                                .setValue(liked).addOnSuccessListener {
                                    Log.i("MATCH", "Match Success\n")
                                    onMatch.invoke(true)
                                    result.status = Constants.CODE_SUCCESS

                                }
                        }
                }


            }
            if (!flag) {
                onMatch.invoke(false)
                result.status = Constants.CODE_SUCCESS
                Log.i("MATCH", "No match Success\n")
            }
        } else {
            onMatch.invoke(false)
            result.status = Constants.CODE_SUCCESS
            Log.i("MATCH", "No match Success Empty Array\n")
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
        val uid = viewModel.currentUser.value.uid
        val reflatitude = database.getReference("/users")
        reflatitude.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.child("${uid}/matches").children
                children.forEach {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("LOAD MATCHES", "Failed to read value.", error.toException())
            }
        })
    }

    override fun getCurrentUser(viewModel: MainViewModel, onFirstLoad: () -> Unit) {
        val uid = getUid()
        val database = Firebase.database
        val reflatitude = database.getReference("/users/$uid")
        reflatitude.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profile = dataSnapshot.getValue(FBUserProfile::class.java)
                if (profile != null) {
                    viewModel.currentUser.value = profile
                }
                print(profile)
                if (viewModel.isFirstLoad.value) {
                    onFirstLoad.invoke()
                    viewModel.isFirstLoad.value = false
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("LOAD MATCHES", "Failed to read value.", error.toException())
            }
        })

    }

}