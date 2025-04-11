package brawijaya.example.literakids.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import brawijaya.example.literakids.ui.screens.profile.UserData
import brawijaya.example.literakids.data.model.UserSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    suspend fun getUserData(): UserData? = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext null

        try {
            // For demo purposes, we'll use the dummy data as defined in the requirements
            val snapshot = database.reference.child("dummyUser").get().await()

            if (snapshot.exists()) {
                val kidData = snapshot.child("akunAnak")
                val parentData = snapshot.child("akunPendamping")

                return@withContext UserData(
                    kidName = kidData.child("namaLengkap").getValue(String::class.java) ?: "",
                    kidUsername = kidData.child("username").getValue(String::class.java) ?: "",
                    kidAvatarId = kidData.child("avatar").getValue(Int::class.java) ?: 1,
                    kidLevel = kidData.child("level").getValue(Int::class.java) ?: 1,
                    kidXp = kidData.child("xp").getValue(Int::class.java) ?: 0,
                    kidCoins = kidData.child("koin").getValue(Int::class.java) ?: 0,
                    parentName = parentData.child("namaLengkap").getValue(String::class.java) ?: "",
                    parentUsername = parentData.child("username").getValue(String::class.java) ?: "",
                    parentAvatarId = parentData.child("avatar").getValue(Int::class.java) ?: 1
                )
            }

            return@withContext null
        } catch (e: Exception) {
            return@withContext null
        }
    }

    suspend fun updateKidAvatar(avatarId: Int): Boolean = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext false

        try {
            database.reference.child("dummyUser").child("akunAnak").child("avatar")
                .setValue(avatarId).await()
            return@withContext true
        } catch (e: Exception) {
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