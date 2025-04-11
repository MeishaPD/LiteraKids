package brawijaya.example.literakids.ui.screens.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.literakids.ui.screens.profile.ProfileViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

data class AvatarOption(
    val id: Int,
    val name: String,
    val price: Int,
    val imageUrl: String
)

@Composable
fun AvatarSelectionScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val avatarOptions = listOf(
        AvatarOption(1, "Penyihir", 0, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F1.png?alt=media&token=cfcb0d86-5030-4f31-878d-d2887f37e788"),
        AvatarOption(2, "Dinosaurus", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F2.png?alt=media&token=47feab1f-e1cd-4b90-b1dd-7310d768da06"),
        AvatarOption(3, "Kuda Poni", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F3.png?alt=media&token=58496966-a513-4adc-8055-691f99340385"),
        AvatarOption(4, "Bunga", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F4.png?alt=media&token=05594a5b-23b6-4abe-97e7-4a8025a16b7d"),
        AvatarOption(5, "Katak", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F5.png?alt=media&token=d858d915-9151-4381-b31e-242e51c67bf0"),
        AvatarOption(6, "Ksatria", 250, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F6.png?alt=media&token=56d67e62-32be-4a23-85f4-d6139183e969")
    )

    var selectedAvatarId by remember { mutableStateOf(uiState.userData.kidAvatarId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Gradient header
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
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White
                )
            }

            Text(
                text = "Kembali",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 56.dp)
            )
        }

        // Selected avatar preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarOptions.find { it.id == selectedAvatarId }?.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Selected Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }

        // Coins display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF5AD8FF),
                                Color(0xFFDE99FF)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Coin",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${uiState.userData.kidCoins} koin",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Avatar selection title
        Text(
            text = "Pilihan Karakter",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF1D7193),
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        // Avatar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(avatarOptions) { avatar ->
                val isSelected = selectedAvatarId == avatar.id
                val isOwned = (avatar.id == 1) || (avatar.price <= uiState.userData.kidCoins)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color(0xFF5DCCF8) else Color.LightGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(enabled = isOwned) {
                            selectedAvatarId = avatar.id
                            viewModel.updateKidAvatar(avatar.id)
                        }
                        .padding(8.dp)
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(avatar.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = avatar.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF5DCCF8))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = avatar.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1D7193)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (avatar.price > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFECC7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Coin",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${avatar.price} Koin",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1D7193)
                                )
                            }
                        }
                    }
                }
            }
        }
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