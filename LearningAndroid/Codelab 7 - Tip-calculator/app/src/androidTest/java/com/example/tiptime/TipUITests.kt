package com.example.tiptime

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class TipUITests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.onNodeWithText("Bill Amount").performTextReplacement("10")
        composeTestRule.onNodeWithText("Tip Percentage").performTextReplacement("20")
        //val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        // 1. Buscamos el nodo
/*        val semanticsNode = composeTestRule
            .onNodeWithText("Tip Amount:", substring = true)
            .fetchSemanticsNode()
        // 2. Extraemos el texto
        val textoObtenido = semanticsNode.config[SemanticsProperties.Text].first().text
        // 3. Imprimimos
        println(">>> TEXTO MOSTRADO EN PANTALLA: $textoObtenido")*/

        composeTestRule.onNodeWithText("Tip Amount: \$2.00")
            .assertExists("No se encontró la vista con la propina calculada")
    }

}