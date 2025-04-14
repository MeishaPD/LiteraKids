package brawijaya.example.literakids.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import brawijaya.example.literakids.data.model.UserData
import brawijaya.example.literakids.data.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    private val users = mutableMapOf<String, UserData>()
    private val userFlows = mutableMapOf<String, MutableStateFlow<Result<UserData>>>()

    init {
        val mockUsers = listOf(
            UserData(
                id = "user11",
                fullName = "Levi Annora",
                username = "leviannora",
                level = 7,
                currentXp = 90,
                maxXp = 100,
                age = 5,
                gender = "perempuan",
                schoolLevel = "TK",
                birthDate = "7/4/2025",
                avatarUrl = "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F1.png?alt=media&token=cfcb0d86-5030-4f31-878d-d2887f37e788",
                coins = 450,
                type = "child",
                ownedAvatars = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F1.png?alt=media&token=cfcb0d86-5030-4f31-878d-d2887f37e788"
                )
            ),
            UserData(
                id = "user12",
                fullName = "Adinda Febyola",
                username = "febydinda",
                level = 38,
                birthDate = "1995-02-20",
                avatarUrl = "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F7.png?alt=media&token=7f31d3b4-55ec-46d5-b463-17e4c2da6929",
                phoneNumber = "082198765432",
                occupation = "Ibu Rumah Tangga",
                relationship = "Ibu",
                type = "parent",
            )
        )

        mockUsers.forEach { user ->
            users[user.id] = user
            userFlows[user.id] = MutableStateFlow(Result.success(user))
        }
    }

    fun getUserById(userId: String): Flow<Result<UserData>> {
        return userFlows[userId] ?: flow {
            emit(Result.failure(Exception("User not found")))
        }
    }

    fun updateUser(userId: String, userUpdates: Map<String, Any>): Result<Unit> {
        return try {
            val currentUser = users[userId] ?: throw Exception("User not found")

            val updatedOwnedAvatars = when (val ownedAvatarsUpdate = userUpdates["ownedAvatars"]) {
                is List<*> -> ownedAvatarsUpdate.filterIsInstance<String>()
                else -> currentUser.ownedAvatars
            }

            val updatedUser = currentUser.copy(
                fullName = (userUpdates["fullName"] as? String) ?: currentUser.fullName,
                username = (userUpdates["username"] as? String) ?: currentUser.username,
                level = (userUpdates["level"] as? Number)?.toInt() ?: currentUser.level,
                currentXp = (userUpdates["currentXp"] as? Number)?.toInt() ?: currentUser.currentXp,
                maxXp = (userUpdates["maxXp"] as? Number)?.toInt() ?: currentUser.maxXp,
                age = when(val age = userUpdates["age"]) {
                    is Number -> age.toInt()
                    is String -> age.toIntOrNull() ?: currentUser.age
                    else -> currentUser.age
                },
                gender = (userUpdates["gender"] as? String) ?: currentUser.gender,
                schoolLevel = (userUpdates["schoolLevel"] as? String) ?: currentUser.schoolLevel,
                birthDate = (userUpdates["birthDate"] as? String) ?: currentUser.birthDate,
                avatarUrl = (userUpdates["avatarUrl"] as? String) ?: currentUser.avatarUrl,
                coins = (userUpdates["coin"] as? Number)?.toInt() ?: currentUser.coins,
                phoneNumber = (userUpdates["phoneNumber"] as? String) ?: currentUser.phoneNumber,
                occupation = (userUpdates["occupation"] as? String) ?: currentUser.occupation,
                relationship = (userUpdates["relationship"] as? String) ?: currentUser.relationship,
                type = (userUpdates["type"] as? String) ?: currentUser.type,
                ownedAvatars = updatedOwnedAvatars
            )

            users[userId] = updatedUser
            userFlows[userId]?.value = Result.success(updatedUser)

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
    suspend fun updateNotificationsEnabled(enabled: Boolean)
    suspend fun updateReadingReminders(enabled: Boolean)
}

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val READING_REMINDERS = booleanPreferencesKey("reading_reminders")
    }

    override fun getUserSettings(): Flow<UserSettings> {
        return dataStore.data.map { preferences ->
            UserSettings(
                isDarkMode = preferences[PreferencesKeys.DARK_MODE] ?: false,
                language = preferences[PreferencesKeys.LANGUAGE] ?: "Indonesia",
                notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true,
                readingReminders = preferences[PreferencesKeys.READING_REMINDERS] ?: true
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

    override suspend fun updateNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    override suspend fun updateReadingReminders(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.READING_REMINDERS] = enabled
        }
    }
}