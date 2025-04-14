package brawijaya.example.literakids.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import brawijaya.example.literakids.ui.screens.profile.ProfileScreen
import brawijaya.example.literakids.ui.screens.auth.LoginScreen
import brawijaya.example.literakids.ui.screens.auth.RegisterScreen
import brawijaya.example.literakids.ui.screens.avatar.AvatarSelectionScreen
import brawijaya.example.literakids.ui.screens.childProfile.ChildProfileScreen
import brawijaya.example.literakids.ui.screens.parentProfile.ParentProfileScreen

sealed class Screen(val route: String) {
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object ParentProfile : Screen("parent_profile")
    object ChildProfile : Screen("child_profile")
    object AvatarSelection : Screen("avatar_selection")
    object Home : Screen("home")
    object Library : Screen("Library")
    object Community : Screen("community")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Profile.route
    ) {
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(Screen.ChildProfile.route) {
            ChildProfileScreen(navController = navController)
        }

        composable(Screen.ParentProfile.route) {
            ParentProfileScreen(navController = navController)
        }

        composable(Screen.AvatarSelection.route) {
            AvatarSelectionScreen(navController = navController)
        }
    }
}