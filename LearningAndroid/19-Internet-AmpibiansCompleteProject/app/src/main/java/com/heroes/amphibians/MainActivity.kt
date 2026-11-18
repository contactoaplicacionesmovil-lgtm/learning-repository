package com.heroes.amphibians

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heroes.amphibians.model.Amphibian
import com.heroes.amphibians.ui.AmphibiansApp
import com.heroes.amphibians.ui.screens.AmphibiansUiState
import com.heroes.amphibians.ui.screens.AmphibiansViewModel
import com.heroes.amphibians.ui.theme.AmphibiansTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmphibiansTheme {

                val amphibiansViewModels: AmphibiansViewModel = viewModel()

                AmphibiansApp(
                    amphibiansUiState = amphibiansViewModels.amphibiansUiState,
                    retryAction = amphibiansViewModels::getAmphibians
                )
            }
        }
    }
}

private val fakeAmphibians = listOf(
    Amphibian(
        id = "1",
        name = "Great Basin Spadefoot",
        type = "Toad",
        description = "This toad spends most of its life underground due to the arid desert conditions in which it lives.",
        imgSrc = ""
    ),
    Amphibian(
        id = "2",
        name = "Roraima Bush Toad",
        type = "Toad",
        description = "This toad is typically found in South America.",
        imgSrc = ""
    )
)

@Preview(showBackground = true)
@Composable
fun AmphibiansAppPreview() {
    AmphibiansTheme {
        AmphibiansApp(
            amphibiansUiState = AmphibiansUiState.Success(fakeAmphibians),
            retryAction = {}
        )
    }
}
