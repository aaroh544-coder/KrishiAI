package com.krishiai.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.krishiai.app.ui.auth.AuthViewModel
import com.krishiai.app.ui.auth.LoginScreen
import com.krishiai.app.ui.auth.ProfileSetupScreen
import com.krishiai.app.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
}

@Composable
@Composable
fun KrishiNavGraph(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val startDestination = if (viewModel.currentUserUid.value != null) Screen.Home.route else Screen.Login.route
    
    NavHost(navController = navController, startDestination = Screen.Login.route) { // Keeping Login as start for now to ensure flow is tested

        
        composable(Screen.Login.route) {
            val viewModel = hiltViewModel<AuthViewModel>()
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNewUser = {
                    navController.navigate(Screen.ProfileSetup.route)
                },
                viewModel = viewModel
            )
        }
        
        composable(Screen.ProfileSetup.route) {
            val viewModel = hiltViewModel<AuthViewModel>()
            ProfileSetupScreen(
                onProfileSaved = {
                     navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
        

        composable(Screen.Home.route) {
            com.krishiai.app.ui.MainScreen(
                onLogout = {
                    viewModel.signOut() // We need a viewModel here or just handle nav
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
