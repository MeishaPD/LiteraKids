package brawijaya.example.literakids.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface AuthRepository {
    val currentUser: FirebaseUser?
    val isUserLoggedIn: Boolean

    suspend fun signUp(email: String, password: String, name: String): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun resetPassword(email: String): AuthResult
    fun signOut()
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    override suspend fun signUp(email: String, password: String, name: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                // Update display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user.updateProfile(profileUpdates).await()

                AuthResult.Success
            } else {
                AuthResult.Error("Pendaftaran gagal, silakan coba lagi")
            }
        } catch (e: Exception) {
            when {
                e.message?.contains("email address is already in use") == true -> {
                    AuthResult.Error("Email sudah terdaftar")
                }
                e.message?.contains("password is invalid") == true -> {
                    AuthResult.Error("Password minimal 6 karakter")
                }
                else -> {
                    AuthResult.Error("Pendaftaran gagal: ${e.message}")
                }
            }
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            when {
                e.message?.contains("no user record") == true -> {
                    AuthResult.Error("Email tidak terdaftar")
                }
                e.message?.contains("password is invalid") == true -> {
                    AuthResult.Error("Password salah")
                }
                else -> {
                    AuthResult.Error("Login gagal: ${e.message}")
                }
            }
        }
    }

    override suspend fun resetPassword(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error("Gagal mengirim email reset password: ${e.message}")
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}