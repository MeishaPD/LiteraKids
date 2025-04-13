package brawijaya.example.literakids.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//import brawijaya.example.literakids.ui.screens.home.HomeScreen
//import brawijaya.example.literakids.ui.screens.library.LibraryScreen
//import brawijaya.example.literakids.ui.screens.community.CommunityScreen
//import brawijaya.example.literakids.ui.screens.profile.ProfileScreen
import brawijaya.example.literakids.ui.screens.auth.LoginScreen
import brawijaya.example.literakids.ui.screens.auth.RegisterScreen
//import brawijaya.example.literakids.ui.screens.avatar.AvatarSelectionScreen

//import brawijaya.example.literakids.ui.screens.auth.RegisterScreen
//import brawijaya.example.literakids.ui.screens.profile.AccountInfoScreen
//import brawijaya.example.literakids.ui.screens.profile.NotificationsScreen
//import brawijaya.example.literakids.ui.screens.profile.PrivacyPolicyScreen
//import brawijaya.example.literakids.ui.screens.profile.TermsOfServiceScreen
//import brawijaya.example.literakids.ui.screens.profile.SecurityScreen
//import brawijaya.example.literakids.ui.screens.profile.AppUpdateScreen

sealed class Screen(val route: String) {
//    object Home : Screen("home")
    object Library : Screen("library")
    object Community : Screen("community")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object AccountInfo : Screen("account_info")
    object Notifications : Screen("notifications")
    object PrivacyPolicy : Screen("privacy_policy")
    object TermsOfService : Screen("terms_of_service")
    object Security : Screen("security")
    object AppUpdate : Screen("app_update")
    object AvatarSelection : Screen("avatar_selection")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Profile.route
    ) {
//        composable(Screen.Home.route) {
//            HomeScreen(navController = navController)
//        }
//
//        composable(Screen.Library.route) {
//            LibraryScreen(navController = navController)
//        }
//
//        composable(Screen.Community.route) {
//            CommunityScreen(navController = navController)
//        }

//        composable(Screen.Profile.route) {
//            ProfileScreen(navController = navController)
//        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
//
//        composable(Screen.AccountInfo.route) {
//            AccountInfoScreen(navController = navController)
//        }
//
//        composable(Screen.Notifications.route) {
//            NotificationsScreen(navController = navController)
//        }
//
//        composable(Screen.PrivacyPolicy.route) {
//            PrivacyPolicyScreen(navController = navController)
//        }
//
//        composable(Screen.TermsOfService.route) {
//            TermsOfServiceScreen(navController = navController)
//        }
//
//        composable(Screen.Security.route) {
//            SecurityScreen(navController = navController)
//        }
//
//        composable(Screen.AppUpdate.route) {
//            AppUpdateScreen(navController = navController)
//        }

//        composable(Screen.AvatarSelection.route) {
//            AvatarSelectionScreen(navController = navController)
//        }
    }
}