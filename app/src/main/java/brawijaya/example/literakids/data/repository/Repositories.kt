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
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
                                    kidName = kidData.child("namaLengkap").getValue(String::class.java) ?: "Levi Annora",
                                    kidUsername = kidData.child("username").getValue(String::class.java) ?: "@leviannora",
                                    kidAvatarId = kidData.child("avatar").getValue(Int::class.java) ?: 1,
                                    kidLevel = kidData.child("level").getValue(Int::class.java) ?: 1,
                                    kidXp = kidData.child("xp").getValue(Int::class.java) ?: 0,
                                    kidCoins = kidData.child("koin").getValue(Int::class.java) ?: 0,
                                    parentName = parentData.child("namaLengkap").getValue(String::class.java) ?: "Adinda Febyola",
                                    parentUsername = parentData.child("username").getValue(String::class.java) ?: "@febydinda",
                                    parentAvatarId = parentData.child("avatar").getValue(Int::class.java) ?: 7,
                                    avatarUrls = avatarUrls
                                )

                                continuation.resume(userData, onCancellation = null)
                            } else {
                                // If no data exists, create dummy data that matches the screenshot
                                val userData = UserData(
                                    kidName = "Levi Annora",
                                    kidUsername = "@leviannora",
                                    kidAvatarId = 1,
                                    kidLevel = 7,
                                    kidXp = 90,
                                    kidCoins = 420,
                                    parentName = "Adinda Febyola",
                                    parentUsername = "@febydinda",
                                    parentAvatarId = 7,
                                    avatarUrls = avatarUrls
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