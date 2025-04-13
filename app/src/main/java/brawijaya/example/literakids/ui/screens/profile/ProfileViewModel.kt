package brawijaya.example.literakids.ui.screens.profile
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import brawijaya.example.literakids.data.model.UserData
//import brawijaya.example.literakids.data.repository.AuthRepository
//import brawijaya.example.literakids.data.repository.SettingsRepository
//import brawijaya.example.literakids.data.repository.UserRepository
//import com.google.firebase.auth.FirebaseAuth
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//data class ProfileUiState(
//    val isLoading: Boolean = false,
//    val isLoggedIn: Boolean = false,
//    val userData: UserData = UserData(),
//    val isDarkMode: Boolean = false,
//    val selectedLanguage: String = "Indonesia",
//    val showLanguageDialog: Boolean = false,
//    val error: String = "",
//)
//
//@HiltViewModel
//class ProfileViewModel @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val userRepository: UserRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(ProfileUiState())
//    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
//
//    init {
//        checkAuthState()
//        if (authRepository.isUserLoggedIn) {
//            loadUserData()
//        }
//    }
//
//    private fun checkAuthState() {
//        _uiState.update { state ->
//            state.copy(isLoggedIn = authRepository.isUserLoggedIn)
//        }
//    }
//
//    private fun loadUserData() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//
//            try {
//                val userData = userRepository.getUserData()
//                _uiState.update { it.copy(
//                    userData = userData,
//                    isLoading = false
//                )}
//            } catch (e: Exception) {
//                _uiState.update { it.copy(
//                    isLoading = false,
//                    error = "Failed to load user data: ${e.message}"
//                )}
//            }
//        }
//    }
//
//    fun toggleDarkMode() {
//        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
//    }
//
//    fun toggleLanguageDialog() {
//        _uiState.update { it.copy(showLanguageDialog = !it.showLanguageDialog) }
//    }
//
//    fun setLanguage(language: String) {
//        _uiState.update { it.copy(selectedLanguage = language) }
//    }
//
//    fun updateKidAvatar(avatarId: Int) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//
//            try {
//                val success = userRepository.updateKidAvatar(avatarId)
//                if (success) {
////                    _uiState.update { state ->
////                        state.copy(
////                            userData = state.userData.copy(kidAvatarId = avatarId),
////                            isLoading = false
////                        )
////                    }
//                } else {
//                    _uiState.update { it.copy(
//                        isLoading = false,
//                        error = "Failed to update avatar"
//                    )}
//                }
//            } catch (e: Exception) {
//                _uiState.update { it.copy(
//                    isLoading = false,
//                    error = "Error updating avatar: ${e.message}"
//                )}
//            }
//        }
//    }
//
//    fun signOut() {
//        authRepository.signOut()
//        _uiState.update { it.copy(isLoggedIn = false) }
//    }
//
//    fun clearError() {
//        _uiState.update { it.copy(error = "") }
//    }
//}