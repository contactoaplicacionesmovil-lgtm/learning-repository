package com.heroes.amphibians.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.heroes.amphibians.model.Amphibian
import com.heroes.amphibians.ui.screens.AmphibiansUiState
import com.heroes.amphibians.ui.screens.HomeScreen
import com.heroes.amphibians.ui.screens.LoginScreen
import com.heroes.amphibians.ui.theme.AmphibiansTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmphibiansApp(
    amphibiansUiState: AmphibiansUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    isLoggingIn: Boolean,
    loginErrorMessage: String?,
    loginAction: (String, String) -> Unit,
) {


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Amphibians",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->


        if (isAuthenticated) {
            HomeScreen(
                amphibiansUiState = amphibiansUiState,
                retryAction = retryAction,
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LoginScreen(
                onLogin = loginAction,
                isLoading = isLoggingIn,
                errorMessage = loginErrorMessage,
                modifier = Modifier.fillMaxSize()
            )
        }

    }
}