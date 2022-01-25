package com.mangofriends.mangoappnewest.presentation.register

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBProfileImageUrl
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBTag
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
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
    val firebaseRepository: FirebaseRepository,

    ) : ViewModel() {

    val state = RegisterState()
    val animationState = AnimationState()

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

    fun isData1Correct(): Boolean {
        return state.emailState.error == null
                && state.password1State.error == null
                && state.nameState.error == null
                && state.nameState.text.isNotBlank()
                && state.emailState.text.isNotBlank()
                && state.password1State.text.isNotBlank()
                && state.password1State.text == state.password2State.text
    }

    fun isData2Correct(): Boolean {
        return state.bioState.error == null
                && state.ageState.error == null
                && state.cityState.error == null
                && state.imageUriState != null
                && state.ageState.text.isNotBlank()
                && state.cityState.text.isNotBlank()
                && state.bioState.text.isNotBlank()
    }

    fun isData3Correct(): Boolean {
        return state.searchCityState.error == null

    }


}

class RegisterState {
    val nameState: ShortASCIIFieldState = ShortASCIIFieldState()
    val emailState: EmailFieldState = EmailFieldState()
    val password1State: PasswordFieldState = PasswordFieldState()
    val password2State: PasswordFieldState = PasswordFieldState()
    val bioState: LongTextFieldState = LongTextFieldState()
    val ageState: AgeFieldState = AgeFieldState()
    val cityState: StandardNameFieldState = StandardNameFieldState()
    val imageUriState: MutableState<Uri?> = mutableStateOf(null)
    val genderState: MutableState<String> = mutableStateOf("-")

    val searchMinAgeState: MutableState<Float> = mutableStateOf(Constants.MIN_AGE)
    val searchMaxAgeState: MutableState<Float> = mutableStateOf(Constants.MAX_AGE)
    val searchCityState: StandardNameFieldState = StandardNameFieldState()
    var searchAgeSliderPositionState = mutableStateOf(Constants.MIN_AGE..Constants.MAX_AGE)
    val searchInterestTagSwitch: MutableState<Boolean> = mutableStateOf(false)

    val uidState = mutableStateOf("")
    val urlState = mutableListOf<FBProfileImageUrl>()
    val tagState = mutableListOf<FBTag>()

    var isLoading = mutableStateOf(false)

    fun getProfile(): DTOUserProfile {
        return DTOUserProfile(
            this.ageState.text.toInt(),
            this.bioState.text,
            this.genderState.value,
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

    fun getCredentials(): UserCredentials {
        return UserCredentials(this.emailState.text, this.password1State.text)
    }
}

data class AnimationState(
    val direction: MutableState<Boolean> = mutableStateOf(true),
    val currentPage: MutableState<String> = mutableStateOf(Screen.RegisterStep1.route)
)