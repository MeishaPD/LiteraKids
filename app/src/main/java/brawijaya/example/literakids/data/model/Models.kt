package brawijaya.example.literakids.data.model

data class UserData(
    val id: String = "",
    val fullName: String = "",
    val username: String = "",
    val level: Int = 1,
    val currentXp: Int = 0,
    val maxXp: Int = 100,
    val age: Int = 0,
    val gender: String? = null,
    val schoolLevel: String = "",
    val birthDate: String = "",
    val avatarUrl: String = "",
    val coins: Int = 0,
    val phoneNumber: String = "",
    val occupation: String = "",
    val relationship: String = "",
    val type: String = ""
)

data class UserSettings(
    val isDarkMode: Boolean = false,
    val language: String = "Indonesia",
    val notificationsEnabled: Boolean = true,
    val readingReminders: Boolean = true
)