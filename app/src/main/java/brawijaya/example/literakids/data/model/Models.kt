package brawijaya.example.literakids.data.model

import java.util.Date

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
    val type: String = ""
)

data class UserSettings(
    val isDarkMode: Boolean = false,
    val language: String = "Indonesia",
    val notificationsEnabled: Boolean = true,
    val readingReminders: Boolean = true
)

data class AkunAnak(
    val id: String,
    val namaLengkap: String,
    val username: String,
    val gender: String,
    val sekolah: String,
    val koin: Int,
    val level: Int,
    val usia: Int,
    val xp: Int,
    val avatar: Int,
    val ttl: Date,
)

data class AkunPendamping(
    val id: String,
    val namaLengkap: String,
    val username: String,
    val hubungan: String,
    val pekerjaan: String,
    val avatar: Int,
    val hp: Int,
    val ttl: Date,
)