package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingPreview()
                }
            }
        }
    }
}

@Composable
fun LimonadeStepsScreen(modifier: Modifier = Modifier) {

    var currentStep by remember { mutableStateOf(1) }
    var squeezeCount by remember { mutableStateOf(0) }
    var requiredSqueezes by remember { mutableStateOf(2) }

    val currentImage = when (currentStep) {
        1 -> R.drawable.lemon_tree
        2 -> R.drawable.lemon_squeeze
        3 -> R.drawable.lemon_drink
        else -> R.drawable.lemon_restart
    }

    val currentText = when (currentStep) {
        1 -> R.string.lemon_select
        2 -> R.string.lemon_squeeze
        3 -> R.string.lemon_drink
        else -> R.string.lemon_empty_glass
    }

    val currentDescription = when (currentStep) {
        1 -> R.string.lemon_tree_content_description
        2 -> R.string.lemon_content_description
        3 -> R.string.lemonade_content_description
        else -> {
            R.string.empty_glass_content_description
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Button(
            onClick = {
                when (currentStep) {
                    1 -> {
                        requiredSqueezes = (2..4).random()
                        squeezeCount = 0
                        currentStep = 2
                    }
                    2 -> {
                        squeezeCount++
                        if (squeezeCount >= requiredSqueezes) {
                            currentStep = 3
                        }
                    }
                    3 -> {
                        currentStep = 4
                    }
                    4 -> {
                        currentStep = 1
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC3E88D)
            )
        ) {
            Image(
                painter = painterResource(currentImage),
                contentDescription = stringResource(currentDescription)
            )
        }

        Text(
            text = stringResource(currentText),
            fontSize = 17.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LemonadeTheme {
        LimonadeStepsScreen(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}