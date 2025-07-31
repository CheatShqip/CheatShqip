package com.cheatshqip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.topappbar.ToskTopAppBar

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToskTheme {
                Scaffold(
                    topBar = {
                        ToskTopAppBar(
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
