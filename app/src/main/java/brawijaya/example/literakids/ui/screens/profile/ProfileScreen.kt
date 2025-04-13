package brawijaya.example.literakids.ui.screens.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.literakids.R
import brawijaya.example.literakids.ui.navigation.Screen
import brawijaya.example.literakids.ui.screens.childProfile.ChildProfileViewModel
import brawijaya.example.literakids.ui.screens.parentProfile.ParentProfileViewModel
import brawijaya.example.literakids.ui.theme.LiteraKidsTheme
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    navController: NavController,
    parentViewModel: ParentProfileViewModel = hiltViewModel(),
    childViewModel: ChildProfileViewModel = hiltViewModel(),
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val childState by childViewModel.state.collectAsState()
    val parentState by parentViewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5AD8FF),
                            Color(0xFFDE99FF)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Profil",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoggedIn) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Akun Anak",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D7193)
                )

                UserProfileCard(
                    name = childState.fullName,
                    username = "@" + childState.username,
                    avatarUrl = childState.avatarUrl,
                    onEditClick = { navController.navigate(Screen.ChildProfile.route) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Akun Orang Tua/Pendamping",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D7193)
                )

                UserProfileCard(
                    name = parentState.fullName,
                    username = "@" + parentState.username,
                    avatarUrl = parentState.avatarUrl,
                    onEditClick = { navController.navigate(Screen.ParentProfile.route) }
                )

            }
        } else {
            Button(
                onClick = {
                    navController.navigate(Screen.Register.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5DCCF8)
                )
            ) {
                Text(
                    text = "Daftar Akun",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Login.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = SolidColor(Color(0xFF5DCCF8))
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF5DCCF8)
                )
            ) {
                Text(
                    text = "Login Akun",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Tentang Akun",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Tentang Akun Saya",
            subtitle = "Informasi Dasar Seputar Akun Pengguna",
            endIcon = Icons.Default.ChevronRight,
//            onClick = { navController.navigate(Screen.AccountInfo.route) }
            onClick = { showUnavailablePageToast(context) },
        )

        ProfileMenuItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifikasi",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Notifikasi",
            subtitle = "Pengaturan Notifikasi Aplikasi",
            endIcon = Icons.Default.ChevronRight,
//            onClick = { navController.navigate(Screen.Notifications.route) }
            onClick = { showUnavailablePageToast(context) },
        )

        ProfileMenuItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Language,
                    contentDescription = "Bahasa",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Bahasa",
            subtitle = "Sesuaikan Bahasa Dengan Preferensimu",
            endIcon = Icons.Default.ExpandMore,
            onClick = { viewModel.toggleLanguageDialog() }
        )

        ProfileMenuToggleItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.DarkMode,
                    contentDescription = "Tampilan",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Tampilan",
            subtitle = "Ubah Tampilan Menjadi Dark Mode",
            isChecked = uiState.isDarkMode,
            onCheckedChange = { viewModel.toggleDarkMode() }
        )

        ProfileMenuItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = "Kebijakan Privasi",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Kebijakan Privasi",
            subtitle = "Penjelasan Perlindungan Data Akun",
            endIcon = Icons.Default.ChevronRight,
//            onClick = { navController.navigate(Screen.PrivacyPolicy.route) }
            onClick = { showUnavailablePageToast(context) },
        )

        ProfileMenuItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = "Syarat Penggunaan",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Syarat Penggunaan",
            subtitle = "Ketentuan & Aturan Penggunaan Aplikasi",
            endIcon = Icons.Default.ChevronRight,
//            onClick = { navController.navigate(Screen.TermsOfService.route) }
            onClick = { showUnavailablePageToast(context) },
        )

        ProfileMenuItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Keamanan",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Keamanan",
            subtitle = "Lakukan Autentikasi Akun Di Sini",
            endIcon = Icons.Default.ChevronRight,
//            onClick = { navController.navigate(Screen.Security.route) }
            onClick = { showUnavailablePageToast(context) },
        )

        ProfileMenuItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Update,
                    contentDescription = "Pembaruan Aplikasi",
                    tint = Color(0xFF5DCCF8),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = "Pembaruan Aplikasi",
            subtitle = "Informasi Versi Dan Fitur Terbaru Aplikasi",
            endIcon = Icons.Default.ChevronRight,
//            onClick = { navController.navigate(Screen.AppUpdate.route) }
            onClick = { showUnavailablePageToast(context) },
        )

        if (uiState.isLoggedIn) {
            ProfileMenuItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Keluar",
                        tint = Color(0xFF5DCCF8),
                        modifier = Modifier.size(24.dp)
                    )
                },
                title = "Keluar",
                subtitle = "Untuk Keluar Akun",
                endIcon = Icons.Default.ChevronRight,
                onClick = { viewModel.signOut() }
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomNavigation(
            currentRoute = Screen.Profile.route,
            onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }

    // Language Dialog
    if (uiState.showLanguageDialog) {
        LanguageSelectionDialog(
            selectedLanguage = uiState.selectedLanguage,
            onLanguageSelected = { viewModel.setLanguage(it) },
            onDismiss = { viewModel.toggleLanguageDialog() }
        )
    }

    // Loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF5DCCF8)
            )
        }
    }
}

@Composable
fun UserProfileCard(
    name: String,
    username: String,
    avatarUrl: String,
    onEditClick: () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF5AD8FF),
            Color(0xFFDE99FF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(gradient)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (avatarUrl.isNotEmpty()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }
    }
}

fun showUnavailablePageToast(context: Context) {
    Toast.makeText(context, "Halaman belum tersedia", Toast.LENGTH_SHORT).show()
}

@Composable
fun ProfileMenuItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    endIcon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFEBF9FF)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D7193)
            )

            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Icon(
            imageVector = endIcon,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun ProfileMenuToggleItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFEBF9FF)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D7193)
            )

            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF5DCCF8),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun BottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .background(Color(0xFFF6F6F6), shape = RoundedCornerShape(50.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Outlined.Home,
            label = "Beranda",
//            isSelected = currentRoute == Screen.Home.route,
//            onClick = { onNavigate(Screen.Home.route) }
            isSelected = false,
            onClick = {  }
        )

        BottomNavItem(
            icon = Icons.Outlined.Book,
            label = "Perpustakaan",
//            isSelected = currentRoute == Screen.Library.route,
//            onClick = { onNavigate(Screen.Library.route) }
            isSelected = false,
            onClick = {  }
        )

        BottomNavItem(
            icon = Icons.Outlined.Groups,
            label = "Komunitas",
//            isSelected = currentRoute == Screen.Community.route,
//            onClick = { onNavigate(Screen.Community.route) }
            isSelected = false,
            onClick = {  }
        )

        BottomNavItem(
            icon = Icons.Outlined.Person,
            label = "Profil",
            isSelected = currentRoute == Screen.Profile.route,
            onClick = { onNavigate(Screen.Profile.route) }
        )
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isFloating: Boolean = false
) {
    val itemColor = if (isSelected) Color(0xFF5DCCF8) else Color(0xFF046588)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .then(
                if (isFloating) Modifier
                    .offset(y = (-16).dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                else Modifier
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected || isFloating) Color(0xFF5DCCF8)
                    else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected || isFloating) Color.White else itemColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun LanguageSelectionDialog(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf("Indonesia", "English", "Arabic", "Spanish", "French")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pilih Bahasa",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                languages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == selectedLanguage,
                            onClick = { onLanguageSelected(language) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF5DCCF8)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = language,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Selesai",
                    color = Color(0xFF5DCCF8),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}