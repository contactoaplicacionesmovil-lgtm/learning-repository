package com.example.tiptime

import org.junit.Assert
import org.junit.Test
import java.text.NumberFormat

class TipCalculatorTests {

    @Test
    fun calculateTip_20PercentNoRoundUp() {
        val amount = 10.0
        val tipPercent = 20.0

        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, roundUp = false)

        println("Esperado: $expectedTip | Obtención real: $actualTip")
        Assert.assertEquals(expectedTip, actualTip)
    }

}