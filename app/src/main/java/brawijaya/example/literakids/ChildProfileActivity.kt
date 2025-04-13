//package brawijaya.example.literakids
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.ui.Modifier
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.navigation.compose.rememberNavController
//import brawijaya.example.literakids.ui.screens.childProfile.ChildProfileScreen
//import brawijaya.example.literakids.ui.screens.childProfile.ChildProfileViewModel
//import brawijaya.example.literakids.ui.theme.LiteraKidsTheme
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class ChildProfileActivity : ComponentActivity() {
//
//    private val viewModel: ChildProfileViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val userId = intent.getStringExtra("USER_ID") ?: "user11"
//        viewModel.setUserId(userId)
//
//        setContent {
//            LiteraKidsTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val navController = rememberNavController()
//                    ChildProfileScreen(
//                        viewModel = viewModel,
//                        onBackClick = { finish() },
//                        navController = navController
//                    )
//                }
//            }
//        }
//    }
//}