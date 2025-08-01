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
            .padding(ToskSpacing.M)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskButton(
            enabled = true,
            color = ToskButtonColor.secondary(),
            contentDescription = "Singular Definite",
            onClick = { },
        ) { Text("Singular Definite") }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskButton(
            enabled = true,
            contentDescription = "Singular Indefinite",
            onClick = { },
        ) { Text("Singular Indefinite") }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskChip(
            enabled = true,
            contentDescription = "Singular Indefinite",
            onClick = { },
        ) { Text("Singular Indefinite") }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskTextField(
            modifier = Modifier,
            value = "Word",
            onValueChange = {  },
            placeholder = { Text("Word") }
        )

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskTextField(
            modifier = Modifier,
            value = "Verb",
            onValueChange = {  },
            placeholder = { Text("Verb") }
        )

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskCard(onClick = {}) {
            Text("Lol")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info1()
        ) {
            Text(text = "NOMINATIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info2()
        ) {
            Text(text = "ACCUSATIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info3()
        ) {
            Text(text = "GENITIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info4()
        ) {
            Text(text = "DATIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info5()
        ) {
            Text(text = "ABLATIVE")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToskTheme {
        Greeting("Android")
    }
}