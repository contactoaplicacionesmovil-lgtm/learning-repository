package com.example.expositordecuadros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
        enableEdgeToEdge() // 👈 Volvemos al enableEdgeToEdge por defecto
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
        Cuadro("La Noche Estrellada", "Vincent van Gogh", "@vangogh_art"),
        Cuadro("La Monalisa", "Leonardo da Vinci", "@davinci_codes"),
        Cuadro("El Grito", "Edvard Munch", "@munch_expression"),
        Cuadro("La Joven de la Perla", "Johannes Vermeer", "@vermeer_gallery"),
        Cuadro("La Persistencia de la Memoria", "Salvador Dalí", "@dali_surreal")
    )
    val pagerState = rememberPagerState(pageCount = { listaCuadros.size })

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val cuadro = listaCuadros[page]

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
                shape = RectangleShape, // 👈 Sin colors personalizados, usa los defaults de Card
                colors = CardDefaults.cardColors(containerColor = Color.White), // 👈 AGREGA ESTA LÍNEA AQUÍ
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()

                        val pageOffset =
                            (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction
                        val alphaShadow = (abs(pageOffset) * 0.6f).coerceIn(0f, 0.7f)

                        drawRect(color = Color.Black.copy(alpha = alphaShadow))
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 1. Título (Sin parámetro color)
                    Text(
                        text = cuadro.titulo,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // 2. Marco del Cuadro (Sin border ni colors personalizados)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .aspectRatio(214f / 236f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFFFD1DC))
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // 3. Autor (Sin parámetro color)
                    Text(
                        text = cuadro.autor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 4. Red Social (Sin parámetro color)
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