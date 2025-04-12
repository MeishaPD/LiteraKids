package brawijaya.example.literakids.ui.screens.parentProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val error: String? = null
)

@HiltViewModel
class ParentProfileViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(ParentProfileState(
        fullName = "Adinda Febyola",
        username = "febydinda",
    ))
    val state: StateFlow<ParentProfileState> = _state.asStateFlow()

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
            _state.update { it.copy(isLoading = true) }

            try {
                _state.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}