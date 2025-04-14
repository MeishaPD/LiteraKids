package brawijaya.example.literakids.ui.screens.avatar

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.literakids.R
import brawijaya.example.literakids.ui.screens.avatar.components.ErrorDialog
import brawijaya.example.literakids.ui.screens.avatar.components.PurchaseDialog
import brawijaya.example.literakids.ui.screens.avatar.components.SuccessDialog
import brawijaya.example.literakids.ui.screens.childProfile.ChildProfileViewModel
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
    viewModel: ChildProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()

    val avatarOptions = listOf(
        AvatarOption(1, "Penyihir", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F1.png?alt=media&token=cfcb0d86-5030-4f31-878d-d2887f37e788"),
        AvatarOption(2, "Dinosaurus", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F2.png?alt=media&token=47feab1f-e1cd-4b90-b1dd-7310d768da06"),
        AvatarOption(3, "Kuda Poni", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F3.png?alt=media&token=58496966-a513-4adc-8055-691f99340385"),
        AvatarOption(4, "Bunga", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F4.png?alt=media&token=05594a5b-23b6-4abe-97e7-4a8025a16b7d"),
        AvatarOption(5, "Katak", 200, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F5.png?alt=media&token=d858d915-9151-4381-b31e-242e51c67bf0"),
        AvatarOption(6, "Ksatria", 250, "https://firebasestorage.googleapis.com/v0/b/fitly-test-app.appspot.com/o/avatars%2F6.png?alt=media&token=56d67e62-32be-4a23-85f4-d6139183e969")
    )

    var selectedAvatar by remember(uiState.avatarUrl) { mutableStateOf(uiState.avatarUrl) }

    var showOnlyOwned by remember { mutableStateOf(false) }

    var showPurchaseDialog by remember { mutableStateOf(false) }
    var selectedAvatarForPurchase by remember { mutableStateOf<AvatarOption?>(null) }

    val findAvatarByUrl: (String) -> AvatarOption? = { url ->
        avatarOptions.find { it.imageUrl == url }
    }

    val currentActionAvatar = selectedAvatarForPurchase ?:
    findAvatarByUrl(uiState.avatarUrl) ?:
    avatarOptions.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedAvatar)
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
                    Image(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "Coin",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${uiState.coins} koin",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = "Pilihan Karakter",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF1D7193),
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        val displayedAvatars = if (showOnlyOwned) {
            avatarOptions.filter { avatar -> avatar.imageUrl in uiState.ownedAvatars }
        } else {
            avatarOptions
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(displayedAvatars) { avatar ->
                val isSelected = selectedAvatar == avatar.imageUrl

                val isOwned = avatar.imageUrl in uiState.ownedAvatars

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
                        .clickable {
                            if (isOwned) {
                                selectedAvatar = avatar.imageUrl
                                viewModel.updateAvatar(avatar.imageUrl)
                            } else {
                                selectedAvatarForPurchase = avatar
                                showPurchaseDialog = true
                            }
                        }
                        .padding(8.dp)
                        .height(140.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.TopEnd,
                        modifier = Modifier.weight(1f)
                    ) {
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
                                .align(Alignment.Center)
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
                        } else if (isOwned) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF43A047))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Owned",
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
                        color = Color(0xFF1D7193),
                        maxLines = 1,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (avatar.price > 0 && !isOwned) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.coin),
                                    contentDescription = "Coin",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${avatar.price} Koin",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1D7193)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(1.dp))
                        }
                    }
                }
            }
        }
    }

    if (showPurchaseDialog && selectedAvatarForPurchase != null) {
        PurchaseDialog(
            avatarOption = selectedAvatarForPurchase!!,
            onDismiss = {
                showPurchaseDialog = false
            },
            onConfirmPurchase = {
                viewModel.purchaseAvatar(
                    selectedAvatarForPurchase!!.imageUrl,
                    selectedAvatarForPurchase!!.price
                )

                showPurchaseDialog = false
            }
        )
    }

    if (uiState.purchaseSuccess) {
        SuccessDialog(
            showDialog = true,
            avatarOption = currentActionAvatar,
            message = "Avatar berhasil dibeli!",
            onDismiss = {
                viewModel.resetPurchaseStatus()
                selectedAvatar = currentActionAvatar.imageUrl
            }
        )
    }

    if (uiState.purchaseError != null) {
        ErrorDialog(
            showDialog = true,
            avatarOption = currentActionAvatar,
            message = uiState.purchaseError ?: "Gagal membeli avatar. Silakan coba lagi.",
            onDismiss = { viewModel.resetPurchaseStatus() }
        )
    }

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