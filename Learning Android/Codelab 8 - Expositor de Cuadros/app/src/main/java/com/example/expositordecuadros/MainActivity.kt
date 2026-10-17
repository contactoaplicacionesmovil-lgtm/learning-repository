package com.example.expositordecuadros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expositordecuadros.ui.theme.ExpositorDeCuadrosTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpositorDeCuadrosTheme {
                Scaffold(
                    containerColor = Color.White,
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    ExpositorCubo3D()
                }
            }
        }
    }
}

@Composable
fun ExpositorCubo3D(modifier: Modifier = Modifier) {
    val listaCuadros = listOf(
        Cuadro("Gengar\nHappy Halloween", "Oliver Hamlin", "@oliver_hamlin", R.drawable.gengar),
        Cuadro("Psyduck calabaza", "Drawzee", "@drazee_", R.drawable.psyduck),
        Cuadro(
            "Mareep", "Dammit.png", "@dammit.png", R.drawable.jirafita
        ),
        Cuadro(
            "Tigresa del Oriente",
            "IA de zombies",
            "@tigresadelorienteclips",
            R.drawable.tigresa
        ),
        Cuadro("Blaziken", "Oliver Hamlin", "@oliver_hamlin", R.drawable.kombusquen),
        Cuadro("Psyduck con sueño", "Porygun", "@porygun", R.drawable.psyduck_sleepy),

        )
    val pageCountVirtual = Int.MAX_VALUE
    val initialPage = (pageCountVirtual / 2) - ((pageCountVirtual / 2) % listaCuadros.size)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { pageCountVirtual }
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val cuadro = listaCuadros[page % listaCuadros.size]

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset =
                        (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction

                    transformOrigin = if (pageOffset < 0) {
                        TransformOrigin(1f, 0.5f)
                    } else {
                        TransformOrigin(0f, 0.5f)
                    }

                    rotationY = pageOffset * 90f
                    cameraDistance = 16 * density
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RectangleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = cuadro.titulo,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .aspectRatio(214f / 236f)
                    ) {
                        Image(
                            painter = painterResource(id = cuadro.imagenRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = cuadro.autor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = cuadro.redSocial,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpositorDeCuadrosTheme {
        ExpositorCubo3D()
    }
}