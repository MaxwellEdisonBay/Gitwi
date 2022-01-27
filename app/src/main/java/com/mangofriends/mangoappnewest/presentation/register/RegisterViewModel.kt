package com.mangofriends.mangoappnewest.presentation.register

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBProfileImageUrl
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBTag
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.use_case.FBRegisterWithCredentialsUseCase
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.input.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val regUseCase: FBRegisterWithCredentialsUseCase,
    val firebaseRepository: FirebaseRepository, application: Application

) : AndroidViewModel(application) {

    val state = RegisterState()
    val animationState = AnimationState()
    val listOfAllImages = mutableStateListOf<Uri>()


    fun performRegister(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit
    ) {
        regUseCase(viewModel, navController, onError).launchIn(viewModelScope)
    }

    fun moveToPage(route: String, direction: Boolean) {
        animationState.direction.value = direction
        animationState.currentPage.value = route
    }

    fun isData2Correct(): Boolean {
        return state.bioState.error == null
                && state.ageState.error == null
                && state.cityState.error == null
                && state.imageUriState.value != null
                && state.ageState.text.isNotBlank()
                && state.cityState.text.isNotBlank()
                && state.bioState.text.isNotBlank()
    }

    fun isData3Correct(): Boolean {
        return state.searchCityState.error == null

    }

    fun getAllShownImagesPath(contentResolver: ContentResolver) {
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val columnIndexID: Int
        val projection = arrayOf(MediaStore.Images.Media._ID)
        var imageId: Long
        listOfAllImages.clear()
        cursor = contentResolver.query(uriExternal, projection, null, null, null)
        if (cursor != null) {
            columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(columnIndexID)
                val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                listOfAllImages.add(uriImage)
            }
            cursor.close()
        }
    }

}

class RegisterState {
    val nameState: ShortASCIIFieldState = ShortASCIIFieldState()
    val emailState: EmailFieldState = EmailFieldState()
    val password1State: PasswordFieldState = PasswordFieldState()
    val password2State: PasswordFieldState = PasswordFieldState()
    val bioState: LongTextFieldState = LongTextFieldState()
    val ageState: AgeFieldState = AgeFieldState()
    val cityState: ShortASCIIFieldState = ShortASCIIFieldState()
    val imageUriState: MutableState<ImageUriState> = mutableStateOf(ImageUriState())
    val genderState: MutableState<GenderState> = mutableStateOf(GenderState())

    val searchMinAgeState: MutableState<Float> = mutableStateOf(Constants.MIN_AGE)
    val searchMaxAgeState: MutableState<Float> = mutableStateOf(Constants.MAX_AGE)
    val searchCityState: ShortASCIIFieldState = ShortASCIIFieldState()
    var searchAgeSliderPositionState = mutableStateOf(Constants.MIN_AGE..Constants.MAX_AGE)
    val searchInterestTagSwitch: MutableState<Boolean> = mutableStateOf(false)

    val uidState = mutableStateOf("")
    val urlState = mutableListOf<FBProfileImageUrl>()
    val tagState = mutableListOf<FBTag>()

    val error = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun getProfile(): DTOUserProfile {
        return DTOUserProfile(
            this.ageState.text.toInt(),
            this.bioState.text,
            this.genderState.value.gender,
            "",
            this.cityState.text,
            this.nameState.text,
            "",
            emptyList(),
            System.currentTimeMillis(),
            emptyList(),
            this.uidState.value,
            "",
            "",

            searchMinAgeState.value.roundToInt(),
            searchMaxAgeState.value.roundToInt(),
            searchCityState.text,
            searchInterestTagSwitch.value

        )
    }
}

data class AnimationState(
    val direction: MutableState<Boolean> = mutableStateOf(true),
    val currentPage: MutableState<String> = mutableStateOf(Screen.RegisterStep1.route)
)

data class ImageUriState(
    val uri: Uri? = null,
    val error: String? = null
)

data class GenderState(
    val gender: String = "-",
    val error: String? = null
)