package support.informatique.fabriquer.realisation.toskdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.badge.ToskBadge
import com.cheatshqip.tosk.badge.tokens.ToskBadgeColor
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.button.tokens.ToskButtonColor
import com.cheatshqip.tosk.card.ToskCard
import com.cheatshqip.tosk.chip.ToskChip
import com.cheatshqip.tosk.textfield.ToskTextField
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToskTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = ToskTheme.colors.background.secondary,
                ) { innerPadding ->
                    MainScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}