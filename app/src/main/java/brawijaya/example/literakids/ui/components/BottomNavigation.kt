package brawijaya.example.literakids.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import brawijaya.example.literakids.ui.navigation.Screen

@Composable
fun BottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .background(Color(0xFFF6F6F6), shape = RoundedCornerShape(50.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Outlined.Home,
            label = "Beranda",
            isSelected = currentRoute == Screen.Home.route,
            onClick = { onNavigate(Screen.Home.route) }
        )

        BottomNavItem(
            icon = Icons.Outlined.Book,
            label = "Perpustakaan",
            isSelected = currentRoute == Screen.Library.route,
            onClick = { onNavigate(Screen.Library.route) }
        )

        BottomNavItem(
            icon = Icons.Outlined.Groups,
            label = "Komunitas",
            isSelected = currentRoute == Screen.Community.route,
            onClick = { onNavigate(Screen.Community.route) }
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