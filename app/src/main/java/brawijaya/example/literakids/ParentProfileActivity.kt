package brawijaya.example.literakids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import brawijaya.example.literakids.ui.screens.parentProfile.ParentProfileScreen
import brawijaya.example.literakids.ui.theme.LiteraKidsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiteraKidsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ParentProfileScreen()
                }
            }
        }
    }
}