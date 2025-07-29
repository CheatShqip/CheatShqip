package support.informatique.fabriquer.realisation.toskdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.button.tokens.ToskButtonColor
import com.cheatshqip.tosk.chip.ToskChip

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Spacer(modifier = Modifier.padding(10.0.dp))

        ToskButton(
            enabled = true,
            color = ToskButtonColor.secondary(),
            contentDescription = "Singular Definite",
            onClick = { },
        ) { Text("Singular Definite") }

        Spacer(modifier = Modifier.padding(10.0.dp))

        ToskButton(
            enabled = true,
            contentDescription = "Singular Indefinite",
            onClick = { },
        ) { Text("Singular Indefinite") }

        Spacer(modifier = Modifier.padding(10.0.dp))

        ToskChip(
            enabled = true,
            contentDescription = "Singular Indefinite",
            onClick = { },
        ) { Text("Singular Indefinite") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToskTheme {
        Greeting("Android")
    }
}