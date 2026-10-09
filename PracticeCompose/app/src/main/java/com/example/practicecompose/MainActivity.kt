package com.example.practicecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicecompose.ui.theme.PracticeComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ComposableGrid(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Article(modifier: Modifier = Modifier) {
    val headerImage = painterResource(R.drawable.bg_compose_background)

    Column(modifier = modifier) {
        Image(
            painter = headerImage,
            contentDescription = null
        )

        Text(
            text = stringResource(R.string.article_title),
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = stringResource(R.string.article_introduction_text),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = stringResource(R.string.article_conclusion_text),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TaskSucceededScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val image = painterResource(R.drawable.ic_task_completed)
        Image(
            painter = image,
            contentDescription = null
        )

        Text(
            text = "All tasks completed",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        Text(
            text = "Nice work!",
            fontSize = 16.sp
        )
    }
}

@Composable
fun ComposableGrid(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val rowModifier = Modifier.weight(1f)

        Row(modifier = rowModifier) {

            val cardModifier = Modifier
                .fillMaxSize()
                .weight(1f)

            ComposableCard(
                color = Color(0xFFEADDFF),
                description = "Displays text and follows the recommended Material Design guidelines.",
                title = "Text composable",
                modifier = cardModifier
            )
            ComposableCard(
                color = Color(0xFFD0BCFF),
                description = "Creates a composable that lays out and draws a given Painter class object.",
                title = "Image composable",
                modifier = cardModifier
            )
        }

        Row(modifier = rowModifier) {
            val cardModifier = Modifier
                .fillMaxSize()
                .weight(1f)

            ComposableCard(
                color = Color(0xFFB69DF8),
                description = "A layout composable that places its children in a horizontal sequence.",
                title = "Row composable",
                modifier = cardModifier
            )
            ComposableCard(
                color = Color(0xFFF6EDFF),
                description = "A layout composable that places its children in a vertical sequence.",
                title = "Column composable",
                modifier = cardModifier
            )
        }
    }

}

@Composable
fun ComposableCard(
    color: Color,
    description: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(text = description)
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_8_PRO)
@Composable
fun GreetingPreview() {
    PracticeComposeTheme {
        ComposableGrid()
    }
}