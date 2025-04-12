package brawijaya.example.literakids.ui.screens.childProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val age: String = "",
    val gender: String? = null,
    val schoolLevel: String = "",
    val birthDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChildProfileViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(ChildProfileState(
        fullName = "Levi Annora",
        username = "leviannora",
        level = 7,
        currentXp = 80,
        maxXp = 100
    ))
    val state: StateFlow<ChildProfileState> = _state.asStateFlow()

    fun updateFullName(name: String) {
        _state.update { it.copy(fullName = name) }
    }

    fun updateAge(age: String) {
        _state.update { it.copy(age = age) }
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
            _state.update { it.copy(isLoading = true) }

            try {
                _state.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}