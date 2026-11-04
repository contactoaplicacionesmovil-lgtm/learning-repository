/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

// TODO: Screen enum
enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    Entree_menu(R.string.choose_entree),
    Side_dish_menu(R.string.choose_side_dish),
    Accompaniment_menu(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}

// TODO: AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp(
    navController: NavHostController = rememberNavController()
) {
    // TODO: Create Controller and initialization
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LunchTrayScreen.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name
    )

    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    fun cancelOrderAndNavigateToStart() {
        viewModel.resetOrder()
        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
    }

    Scaffold(
        topBar = {
            // TODO: AppBar
            CenterAlignedTopAppBar(
                title = { Text(stringResource(currentScreen.title)) }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()


        // TODO: Navigation host
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = LunchTrayScreen.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(LunchTrayScreen.Entree_menu.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable(route = LunchTrayScreen.Entree_menu.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = ::cancelOrderAndNavigateToStart,
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Side_dish_menu.name) },
                    onSelectionChanged = { viewModel.updateEntree(it) }
                )
            }

            composable(route = LunchTrayScreen.Side_dish_menu.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = ::cancelOrderAndNavigateToStart,
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Accompaniment_menu.name) },
                    onSelectionChanged = { item ->
                        viewModel.updateSideDish(item)
                    }
                )
            }

            composable(route = LunchTrayScreen.Accompaniment_menu.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = ::cancelOrderAndNavigateToStart,
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Checkout.name) },
                    onSelectionChanged = viewModel::updateAccompaniment
                )
            }

            composable(route = LunchTrayScreen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = ::cancelOrderAndNavigateToStart,
                    onCancelButtonClicked = ::cancelOrderAndNavigateToStart
                )
            }

        }
    }
}

