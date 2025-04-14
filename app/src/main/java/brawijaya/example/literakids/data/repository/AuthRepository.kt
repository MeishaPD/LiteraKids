package brawijaya.example.literakids.data.repository

import javax.inject.Inject
import javax.inject.Singleton

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

data class MockFirebaseUser(
    val uid: String,
    val email: String,
    val displayName: String
)

interface AuthRepository {
    val currentUser: MockFirebaseUser?
    val isUserLoggedIn: Boolean

    suspend fun signUp(email: String, password: String, name: String): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun resetPassword(email: String): AuthResult
    fun signOut()
}

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private var _currentUser: MockFirebaseUser = MockFirebaseUser(
        uid = "user11",
        email = "leviannora@mail.com",
        displayName = "Levi Annora"
    )

    private var _isUserLoggedIn: Boolean = true

    override val currentUser: MockFirebaseUser?
        get() = _currentUser

    override val isUserLoggedIn: Boolean
        get() = _isUserLoggedIn

    override suspend fun signUp(email: String, password: String, name: String): AuthResult {
        _isUserLoggedIn = true
        return AuthResult.Success
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        _isUserLoggedIn = true
        return AuthResult.Success
    }

    override suspend fun resetPassword(email: String): AuthResult {
        return AuthResult.Success
    }

    override fun signOut() {
         _isUserLoggedIn = false
    }
}