package brawijaya.example.literakids.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.literakids.data.repository.AuthRepository
import brawijaya.example.literakids.data.repository.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        _uiState.update { state ->
            state.copy(isLoggedIn = authRepository.isUserLoggedIn)
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { state ->
                state.copy(error = "Email dan password tidak boleh kosong")
            }
            return
        }

        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        viewModelScope.launch {
            when (val result = authRepository.signIn(email, password)) {
                is AuthResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            error = ""
                        )
                    }
                }
                is AuthResult.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            _uiState.update { state ->
                state.copy(error = "Semua field harus diisi")
            }
            return
        }

        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        viewModelScope.launch {
            when (val result = authRepository.signUp(email, password, name)) {
                is AuthResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            error = ""
                        )
                    }
                }
                is AuthResult.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _uiState.update { state ->
                state.copy(error = "Email tidak boleh kosong")
            }
            return
        }

        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        viewModelScope.launch {
            when (val result = authRepository.resetPassword(email)) {
                is AuthResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = ""
                        )
                    }
                }
                is AuthResult.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { state ->
            state.copy(error = "")
        }
    }
}