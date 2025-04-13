//package brawijaya.example.literakids
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.viewModels
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.ui.Modifier
//import brawijaya.example.literakids.ui.screens.parentProfile.ParentProfileScreen
//import brawijaya.example.literakids.ui.screens.parentProfile.ParentProfileViewModel
//import brawijaya.example.literakids.ui.theme.LiteraKidsTheme
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class ParentProfileActivity : ComponentActivity() {
//
//    private val viewModel: ParentProfileViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val userId = intent.getStringExtra("USER_ID") ?: "user12"
//        viewModel.setUserId(userId)
//
//        setContent {
//            LiteraKidsTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    ParentProfileScreen(
//                        viewModel = viewModel,
//                        onBackClick = { finish() }
//                    )
//                }
//            }
//        }
//    }
//}