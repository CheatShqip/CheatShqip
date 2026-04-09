package com.cheatshqip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cheatshqip.tosk.LocalToskShowCursor
import com.cheatshqip.tosk.ToskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalToskShowCursor provides SHOW_CURSOR) {
                ToskTheme {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
private fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreenRoute(
                onTranslationClicked = { translation ->
                    navController.navigate("detail/${translation.value}")
                },
            )
        }
        composable(
            route = "detail/{word}",
            arguments = listOf(navArgument("word") { type = NavType.StringType }),
        ) {
            WordDetailScreenRoute(onBack = { navController.popBackStack() })
        }
    }
}
