package brawijaya.example.literakids.ui.screens.childProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.literakids.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChildProfileState(
    val fullName: String = "",
    val username: String = "",
    val level: Int = 1,
    val currentXp: Int = 0,
    val maxXp: Int = 100,
    val age: Int = 0,
    val coins: Int = 0,
    val gender: String? = null,
    val schoolLevel: String = "",
    val birthDate: String = "",
    val isLoading: Boolean = false,
    val avatarUrl: String = "",
    val error: String? = null,
    val updateSuccess: Boolean = false
)

@HiltViewModel
class ChildProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChildProfileState(isLoading = true))
    val state: StateFlow<ChildProfileState> = _state.asStateFlow()

    private var userId: String = "user11"

    init {
        loadUserProfile()
    }

    fun setUserId(id: String) {
        userId = id
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.getUserById(userId).collect { result ->
                result.fold(
                    onSuccess = { user ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                fullName = user.fullName,
                                username = user.username,
                                level = user.level,
                                currentXp = user.currentXp,
                                maxXp = user.maxXp,
                                age = user.age,
                                coins = user.coins,
                                gender = user.gender,
                                schoolLevel = user.schoolLevel,
                                birthDate = user.birthDate,
                                avatarUrl = user.avatarUrl,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load user profile"
                            )
                        }
                    }
                )
            }
        }
    }

    fun updateAvatar(avatarUrl: String) {
        _state.update { it.copy(avatarUrl = avatarUrl) }
    }

    fun updateFullName(name: String) {
        _state.update { it.copy(fullName = name) }
    }

    fun updateAge(age: String) {
        try {
            val ageInt = age.trim().toIntOrNull() ?: 0
            _state.update { it.copy(age = ageInt) }
        } catch (e: NumberFormatException) {
            _state.update { it.copy(error = "Please enter a valid age") }
        }
    }


    fun updateGender(gender: String) {
        _state.update { it.copy(gender = gender) }
    }

    fun updateSchoolLevel(level: String) {
        _state.update { it.copy(schoolLevel = level) }
    }

    fun updateBirthDate(date: String) {
        _state.update { it.copy(birthDate = date) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, updateSuccess = false, error = null) }

            try {
                val updates = mapOf(
                    "fullName" to state.value.fullName,
                    "age" to state.value.age.toString(),
                    "gender" to state.value.gender,
                    "schoolLevel" to state.value.schoolLevel,
                    "birthDate" to state.value.birthDate
                )

                val result = userRepository.updateUser(userId, updates)

                result.fold(
                    onSuccess = {
                        _state.update { it.copy(isLoading = false, updateSuccess = true, error = null) }

                        viewModelScope.launch {
                            kotlinx.coroutines.delay(3000)
                            _state.update { it.copy(updateSuccess = false) }
                        }
                    },
                    onFailure = { e ->
                        _state.update { it.copy(isLoading = false, updateSuccess = false, error = e.message) }
                    }
                )
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, updateSuccess = false, error = e.message) }
            }
        }
    }

    fun resetUpdateSuccess() {
        _state.update { it.copy(updateSuccess = false) }
    }
}