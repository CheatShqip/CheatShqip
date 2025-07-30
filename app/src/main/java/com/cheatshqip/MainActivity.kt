package com.cheatshqip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.cheatshqip.tosk.ToskTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToskTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors =
                                TopAppBarDefaults.topAppBarColors(
                                    containerColor = ToskTheme.colors.background.primary,
                                    titleContentColor = ToskTheme.colors.text.textOnPrimary,
                                ),
                            title = {
                                Text(resources.getString(R.string.app_name))
                            }
                        )
                    },
                ) { innerPadding ->
                    HomeScreenRoute(innerPadding)
                }
            }
        }
    }
}
