package brawijaya.example.literakids.data.model

import java.util.Date

data class UserData(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val parentName: String = "",
    val childAge: Int = 0,
    val readingInterests: List<String> = emptyList(),
    val createdAt: Long = 0
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