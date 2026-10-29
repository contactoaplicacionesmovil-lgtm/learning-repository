package com.example.unscramble.data.ui.test

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Assert
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }

    @Test
    fun gameViewModel_CorrectWordGuesses_ScoreUpdateAndErrorFlagUnset() {

        //currentScrambledWord = LA PALABRITA AUTOGENERADA A ADIVINAR
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        Assert.assertFalse(currentGameUiState.isGuessedWordWrong)
        Assert.assertEquals(SCORE_INCREASE, currentGameUiState.score)
        Assert.assertTrue(currentGameUiState.currentWordCount == 2)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        //given incorrect word as Input
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        Assert.assertEquals(0, currentGameUiState.score)
        Assert.assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val palabraBienEscrita = getUnscrambledWord(gameUiState.currentScrambledWord)

        //verificar que todo se inicializa bien
        Assert.assertNotEquals(palabraBienEscrita, gameUiState.currentScrambledWord)
        Assert.assertTrue(gameUiState.currentWordCount == 1)
        Assert.assertTrue(gameUiState.score == 0)
        Assert.assertFalse(gameUiState.isGuessedWordWrong)
        Assert.assertFalse(gameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_TodasLasPalabrasAcertadas_UiStateCorrectoCuandoElJuegoAcaba() {

        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS) {
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            expectedScore += SCORE_INCREASE
            currentGameUiState = viewModel.uiState.value
            Assert.assertEquals(expectedScore, currentGameUiState.score)

            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        }

        println("Assert WordCount (EXPECTED=10/CURRENT_VALUE): $MAX_NO_OF_WORDS, ${currentGameUiState.currentWordCount}")
        Assert.assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)

        println("Assert GameOver (EXPECTED=TRUE): ${currentGameUiState.isGameOver}")
        Assert.assertTrue(currentGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_SiguientePalabra_PuntajeNocambiaYContadorDePregAumenta() {
        var variablesDeLaVista = viewModel.uiState.value
        val palabraBienEscrita = getUnscrambledWord(variablesDeLaVista.currentScrambledWord)
        viewModel.updateUserGuess(palabraBienEscrita)
        viewModel.checkUserGuess()

        val contadorAntesDelSkip = viewModel.uiState.value.currentWordCount
        viewModel.skipWord()
        variablesDeLaVista = viewModel.uiState.value

        // Assert that score remains unchanged after word is skipped.
        Assert.assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, variablesDeLaVista.score)

        // Assert that word count is increased by 1 after word is skipped.
        Assert.assertEquals(contadorAntesDelSkip + 1, variablesDeLaVista.currentWordCount)
    }

}