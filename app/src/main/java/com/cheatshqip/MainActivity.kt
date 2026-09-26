package com.cheatshqip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cheatshqip.appupdate.presentation.AppUpdateGate
import com.cheatshqip.tosk.LocalToskShowCursor
import com.cheatshqip.tosk.ToskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalToskShowCursor provides SHOW_CURSOR) {
                ToskTheme {
                    AppUpdateGate {
                        AppNavHost()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = selectedDestination(backStackEntry?.destination?.route)

    Scaffold(
        containerColor = ToskTheme.colors.background.secondary,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AppNavBar(
                currentDestination = currentDestination,
                onDestinationSelected = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Translate.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppDestination.Translate.route) {
                HomeScreenRoute(
                    onTranslationClicked = { translation ->
                        navController.navigate("detail/${translation.value}")
                    },
                )
            }
            composable(AppDestination.Decline.route) {
                DeclineScreenRoute()
            }
            composable(AppDestination.Conjugate.route) {
                ConjugateScreenRoute()
            }
            composable(
                route = "detail/{word}",
                arguments = listOf(navArgument("word") { type = NavType.StringType }),
            ) {
                WordDetailScreenRoute(onBack = { navController.popBackStack() })
            }
        }
    }
}

private fun selectedDestination(route: String?): AppDestination =
    when (route) {
        AppDestination.Decline.route -> AppDestination.Decline
        AppDestination.Conjugate.route -> AppDestination.Conjugate
        else -> AppDestination.Translate
    }
