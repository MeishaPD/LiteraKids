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
    val ownedAvatars: List<String> = emptyList(),
    val error: String? = null,
    val updateSuccess: Boolean = false,
    val purchaseSuccess: Boolean = false,
    val purchaseError: String? = null
)

@HiltViewModel
class ChildProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChildProfileState(isLoading = true))
    val state: StateFlow<ChildProfileState> = _state.asStateFlow()

    private var userId: String = "user11"

    private val defaultAvatarUrl = "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F1.png?alt=media&token=cfcb0d86-5030-4f31-878d-d2887f37e788"

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.getUserById(userId).collect { result ->
                result.fold(
                    onSuccess = { user ->
                        val ownedAvatarsList = if (user.ownedAvatars.isNotEmpty()) {
                            user.ownedAvatars.toMutableList()
                        } else {
                            mutableListOf(defaultAvatarUrl)
                        }

                        if (user.avatarUrl.isNotEmpty() && user.avatarUrl != defaultAvatarUrl && !ownedAvatarsList.contains(user.avatarUrl)) {
                            ownedAvatarsList.add(user.avatarUrl)
                        }

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
                                ownedAvatars = ownedAvatarsList,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                ownedAvatars = listOf(defaultAvatarUrl),
                                error = e.message ?: "Failed to load user profile"
                            )
                        }
                    }
                )
            }
        }
    }

    fun updateAvatar(avatarUrl: String) {
        if (avatarUrl in _state.value.ownedAvatars) {
            _state.update { it.copy(avatarUrl = avatarUrl) }
        }
    }

    fun purchaseAvatar(avatarUrl: String, price: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    purchaseSuccess = false,
                    purchaseError = null
                )
            }

            try {
                if (_state.value.coins < price) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            purchaseError = "Koin tidak cukup untuk membeli avatar ini"
                        )
                    }
                    return@launch
                }

                if (avatarUrl in _state.value.ownedAvatars) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            purchaseError = "Avatar ini sudah dimiliki"
                        )
                    }
                    return@launch
                }

                val updatedOwnedAvatars = _state.value.ownedAvatars.toMutableList()
                updatedOwnedAvatars.add(avatarUrl)

                val updates = mapOf(
                    "avatarUrl" to avatarUrl,
                    "coin" to (_state.value.coins - price),
                    "ownedAvatars" to updatedOwnedAvatars
                )

                val result = userRepository.updateUser(userId, updates)

                result.fold(
                    onSuccess = {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                avatarUrl = avatarUrl,
                                coins = currentState.coins - price,
                                ownedAvatars = updatedOwnedAvatars,
                                purchaseSuccess = true
                            )
                        }

                        viewModelScope.launch {
                            kotlinx.coroutines.delay(3000)
                            _state.update { it.copy(purchaseSuccess = false) }
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                purchaseError = e.message ?: "Gagal membeli avatar"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        purchaseError = e.message ?: "Terjadi kesalahan saat membeli avatar"
                    )
                }
            }
        }
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

    fun resetPurchaseStatus() {
        _state.update {
            it.copy(
                purchaseSuccess = false,
                purchaseError = null
            )
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, updateSuccess = false, error = null) }

            try {
                val updates: Map<String, Any> = mapOf(
                    "fullName" to state.value.fullName,
                    "age" to state.value.age,
                    "gender" to (state.value.gender ?: ""),
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
