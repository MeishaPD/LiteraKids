package brawijaya.example.literakids.ui.screens.parentProfile

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

data class ParentProfileState(
    val fullName: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val occupation: String = "",
    val relationship: String = "",
    val birthDate: String = "",
    val isLoading: Boolean = false,
    val avatarUrl: String = "",
    val error: String? = null,
    val updateSuccess: Boolean = false
)

@HiltViewModel
class ParentProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ParentProfileState(isLoading = true))
    val state: StateFlow<ParentProfileState> = _state.asStateFlow()

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
                        if (user.type == "parent") {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    fullName = user.fullName,
                                    username = user.username,
                                    phoneNumber = user.phoneNumber,
                                    occupation = user.occupation,
                                    relationship = user.relationship,
                                    birthDate = user.birthDate,
                                    avatarUrl = user.avatarUrl,
                                    error = null
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Invalid user type for parent profile"
                                )
                            }
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

    fun updateFullName(name: String) {
        _state.update { it.copy(fullName = name) }
    }

    fun updatePhoneNumber(phone: String) {
        _state.update { it.copy(phoneNumber = phone) }
    }

    fun updateOccupation(occupation: String) {
        _state.update { it.copy(occupation = occupation) }
    }

    fun updateRelationship(relationship: String) {
        _state.update { it.copy(relationship = relationship) }
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
                    "phoneNumber" to state.value.phoneNumber,
                    "occupation" to state.value.occupation,
                    "relationship" to state.value.relationship,
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