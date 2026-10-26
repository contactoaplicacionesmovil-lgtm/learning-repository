package com.heroes.myapplication.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heroes.myapplication.ui.components.DynamicCardGrid
import com.heroes.myapplication.ui.components.TarjetaImagenDerecha
import com.heroes.myapplication.ui.components.TarjetaImagenIzquierda

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        DynamicCardGrid(
            itemsList = uiState.items,
            modifier = Modifier.padding(padding)
        )
    }

}