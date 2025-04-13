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
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    suspend fun getUserData(): UserData = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: "dummyUser"

        return@withContext suspendCancellableCoroutine { continuation ->
            // First, get the avatar URLs
            database.reference.child("images").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(imagesSnapshot: DataSnapshot) {
                    val avatarUrls = mutableListOf<String>()

                    // Convert the images data to a list
                    imagesSnapshot.children.forEach { childSnapshot ->
                        childSnapshot.getValue(String::class.java)?.let { avatarUrls.add(it) }
                    }

                    // Now get the user data
                    database.reference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(userSnapshot: DataSnapshot) {
                            if (userSnapshot.exists()) {
                                val kidData = userSnapshot.child("akunAnak")
                                val parentData = userSnapshot.child("akunPendamping")

                                val userData = UserData(
                                    fullName = kidData.child("namaLengkap").getValue(String::class.java) ?: "Levi Annora",
                                    username = kidData.child("username").getValue(String::class.java) ?: "@leviannora",
                                    avatarUrl = kidData.child("avatar").getValue(String::class.java) ?: "11",
                                    level = kidData.child("level").getValue(Int::class.java) ?: 1,
                                    currentXp = kidData.child("xp").getValue(Int::class.java) ?: 0,
                                    coins = kidData.child("koin").getValue(Int::class.java) ?: 0
                                    // parentName = parentData.child("namaLengkap").getValue(String::class.java) ?: "Adinda Febyola",
                                   //  parentUsername = parentData.child("username").getValue(String::class.java) ?: "@febydinda",
                                    // parentAvatarId = parentData.child("avatar").getValue(Int::class.java) ?: 7,
                                    // avatarUrls = avatarUrls
                                )

                                continuation.resume(userData, onCancellation = null)
                            } else {
                                // If no data exists, create dummy data that matches the screenshot
                                val userData = UserData(
                                    fullName = "Levi Annora",
                                    username = "@leviannora",
                                    avatarUrl = "1",
                                    level = 7,
                                    currentXp = 90,
                                    coins = 420,
//                                    parentName = "Adinda Febyola",
//                                    parentUsername = "@febydinda",
//                                    parentAvatarId = 7,
//                                    avatarUrls = avatarUrls
                                )

                                continuation.resume(userData, onCancellation = null)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            continuation.resumeWithException(error.toException())
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }

    suspend fun updateKidAvatar(avatarId: Int): Boolean = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: "dummyUser"

        try {
            database.reference.child(userId).child("akunAnak").child("avatar")
                .setValue(avatarId).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating avatar", e)
            return@withContext false
        }
    }

    // CHILD PROFILE ACTIVITY

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
                            val user = UserData(
                                id = userId,
                                fullName = userData["fullName"] as? String ?: "",
                                username = userData["username"] as? String ?: "",
                                level = (userData["level"] as? Long)?.toInt() ?: 1,
                                currentXp = (userData["currentXp"] as? Long)?.toInt() ?: 80,
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
                                avatarUrl = userData["avatarUrl"] as? String ?: "",
                                type = userData["type"] as? String ?: ""
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

    suspend fun updateUser(userId: String, userUpdates: Map<String, String?>): Result<Unit> {
        return try {
            val userRef = database.getReference("users").child(userId)
            userRef.updateChildren(userUpdates)
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