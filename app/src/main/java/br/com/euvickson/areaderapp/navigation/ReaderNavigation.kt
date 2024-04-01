package br.com.euvickson.areaderapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.euvickson.areaderapp.screens.ReaderSplashScreen
import br.com.euvickson.areaderapp.screens.details.BookDetailsScreen
import br.com.euvickson.areaderapp.screens.home.Home
import br.com.euvickson.areaderapp.screens.login.ReaderLoginScreen
import br.com.euvickson.areaderapp.screens.search.BookSearchViewModel
import br.com.euvickson.areaderapp.screens.search.SearchScreen
import br.com.euvickson.areaderapp.screens.stats.ReaderStatsScreen
import br.com.euvickson.areaderapp.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {

        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderHomeScreen.name) {
            Home(navController = navController)
        }

        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }

        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }

        composable(ReaderScreens.DetailScreen.name) {
            BookDetailsScreen(navController = navController)
        }

        composable(ReaderScreens.UpdateScreen.name) {
            BookUpdateScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }

    }

}