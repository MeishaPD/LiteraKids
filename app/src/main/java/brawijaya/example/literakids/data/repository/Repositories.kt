package brawijaya.example.literakids.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import brawijaya.example.literakids.data.model.UserData
import brawijaya.example.literakids.data.model.UserSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun getUserById(userId: String): Flow<Result<UserData>> = callbackFlow {
        val userRef = database.getReference("users").child(userId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        val typeIndicator = object : GenericTypeIndicator<HashMap<String, Any>>() {}
                        val userData = snapshot.getValue(typeIndicator)

                        Log.d("TAG", userData.toString())
                        if (userData != null) {
                            val ownedAvatarsList = mutableListOf<String>()

                            when (val ownedAvatarsData = userData["ownedAvatars"]) {
                                is List<*> -> {
                                    ownedAvatarsData.forEach { item ->
                                        if (item is String) {
                                            ownedAvatarsList.add(item)
                                        }
                                    }
                                }
                                is ArrayList<*> -> {
                                    ownedAvatarsData.forEach { item ->
                                        if (item is String) {
                                            ownedAvatarsList.add(item)
                                        }
                                    }
                                }
                                is HashMap<*, *> -> {
                                    ownedAvatarsData.values.forEach { item ->
                                        if (item is String) {
                                            ownedAvatarsList.add(item)
                                        }
                                    }
                                }
                            }

                            val avatarUrl = userData["avatarUrl"] as? String ?: ""
                            if (avatarUrl.isNotEmpty() && !ownedAvatarsList.contains(avatarUrl)) {
                                ownedAvatarsList.add(avatarUrl)
                            }

                            if (ownedAvatarsList.isEmpty()) {
                                val defaultAvatarUrl = "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F1.png?alt=media&token=cfcb0d86-5030-4f31-878d-d2887f37e788"
                                ownedAvatarsList.add(defaultAvatarUrl)
                            }

                            val user = UserData(
                                id = userId,
                                fullName = userData["fullName"] as? String ?: "",
                                username = userData["username"] as? String ?: "",
                                level = (userData["level"] as? Long)?.toInt() ?: 1,
                                currentXp = (userData["currentXp"] as? Long)?.toInt() ?: 0,
                                maxXp = (userData["maxXp"] as? Long)?.toInt() ?: 100,
                                age = when (val age = userData["age"]) {
                                    is Long -> age.toInt()
                                    is Int -> age
                                    is String -> age.toIntOrNull() ?: 0
                                    is Double -> age.toInt()
                                    else -> 0
                                },
                                gender = userData["gender"] as? String,
                                schoolLevel = userData["schoolLevel"] as? String ?: "",
                                birthDate = userData["birthDate"] as? String ?: "",
                                avatarUrl = avatarUrl,
                                phoneNumber = userData["phoneNumber"] as? String ?: "",
                                occupation = userData["occupation"] as? String ?: "",
                                relationship = userData["relationship"] as? String ?: "",
                                coins = (userData["coin"] as? Long)?.toInt() ?: 0,
                                type = userData["type"] as? String ?: "",
                                ownedAvatars = ownedAvatarsList  // Add owned avatars to user data
                            )

                            trySend(Result.success(user))
                        } else {
                            trySend(Result.failure(Exception("Failed to parse user data")))
                        }
                    } catch (e: Exception) {
                        trySend(Result.failure(e))
                    }
                } else {
                    trySend(Result.failure(Exception("User not found")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(Exception(error.message)))
            }
        }

        userRef.addValueEventListener(listener)

        awaitClose {
            userRef.removeEventListener(listener)
        }
    }

    suspend fun updateKidAvatar(avatarUrl: String): Boolean = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: "user11"

        try {
            database.reference.child("users").child(userId).child("avatarUrl")
                .setValue(avatarUrl).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating avatar", e)
            return@withContext false
        }
    }

    fun updateUser(userId: String, userUpdates: Map<String, Any>): Result<Unit> {
        return try {
            val userRef = database.getReference("users").child(userId)
            userRef.updateChildren(userUpdates as Map<String, Any?>)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


// Settings Repository
interface SettingsRepository {
    fun getUserSettings(): Flow<UserSettings>
    suspend fun updateDarkMode(isDarkMode: Boolean)
    suspend fun updateLanguage(language: String)
}

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
    }

    override fun getUserSettings(): Flow<UserSettings> {
        return dataStore.data.map { preferences ->
            UserSettings(
                isDarkMode = preferences[PreferencesKeys.DARK_MODE] ?: false,
                language = preferences[PreferencesKeys.LANGUAGE] ?: "Indonesia"
            )
        }
    }

    override suspend fun updateDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = isDarkMode
        }
    }

    override suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }
}