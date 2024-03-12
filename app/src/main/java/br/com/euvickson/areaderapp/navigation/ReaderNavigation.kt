package br.com.euvickson.areaderapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.euvickson.areaderapp.screens.ReaderSplashScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {

        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderHomeScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.SearchScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.LoginScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.DetailScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.UpdateScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

    }

}