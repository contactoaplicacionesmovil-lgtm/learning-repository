package test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import com.example.cupcake.CupcakeScreen
import com.example.cupcake.R
import com.example.cupcake.ui.OrderViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class CupcakeScreenNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CupcakeApp(navController = navController)
        }
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun cupcakeNavHost_clickBackButtonOnFlavorScreen_verifyStartScreenIsShown(){
        composeTestRule.onNodeWithStringId(R.string.one_cupcake).performClick()
        performNavigateUp()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    private fun performNavigateUp(){
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }

    @Test
    fun cupcakeNavHost_clickOneCupcake_navigatesToSelectFlavorScreen() {
        composeTestRule.onNodeWithStringId(R.string.one_cupcake).performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name)
    }

    @Test
    fun cupcakeNavHost_clickOneCupcakeAndClickOnChocolate_navigateToPickUpScreen() {
        navigateToPickUpScreen()
        navController.assertCurrentRouteName(CupcakeScreen.Pickup.name)
    }

    private fun navigateToPickUpScreen() {
        composeTestRule.onNodeWithStringId(R.string.one_cupcake).performClick()
        composeTestRule.onNodeWithStringId(R.string.chocolate).performClick()
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
    }

    @Test
    fun cupcakeNavHost_clickOnDate_navigatesToSummaryScreen() {
        // 1. Instancias el ViewModel
        val viewModel = OrderViewModel()

        // 2. Obtienes la lista de fechas o la primera opción
        val pickupDates = viewModel.uiState.value.pickupOptions
        val firstDateOption = pickupDates[0] // Ejemplo: "Lun may 13"

        navigateToSummaryScreen(firstDateOption)
        navController.assertCurrentRouteName(CupcakeScreen.Summary.name)
    }

    private fun navigateToSummaryScreen(date: String) {
        navigateToPickUpScreen()
        composeTestRule.onNodeWithText(date).performClick()
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
    }



}