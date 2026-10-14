package com.example.expositordecuadros

import android.R.attr.rotationY
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpositorCubo3D(

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ExpositorCubo3D( modifier: Modifier = Modifier) {
    val listaCuadros = listOf(
        Cuadro("La Noche Estrellada", "Vincent van Gogh", "1889", Color(0xFF1E3A8A)),
        Cuadro("La Monalisa", "Leonardo da Vinci", "1503", Color(0xFF3F2E1E)),
        Cuadro("El Grito", "Edvard Munch", "1893", Color(0xFF7C2D12)),
        Cuadro("La Joven de la Perla", "Johannes Vermeer", "1665", Color(0xFF1E293B)),
        Cuadro("La Persistencia de la Memoria", "Salvador Dalí", "1931", Color(0xFF78350F))
    )

    val pagerState = rememberPagerState(pageCount = { listaCuadros.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page -> // Variable 'page': El índice de la tarjeta actual que Compose está dibujando

        val cuadro = listaCuadros[page] // Extraemos la obra de arte según el índice 'page'

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .graphicsLayer {
                    // Variable 3: Distancia decimal de la tarjeta respecto al centro de la pantalla
                    val pageOffset =
                        (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction//*************

                    // Variable 4: transformOrigin (El eje/borde sobre el que gira el cubo)
                    transformOrigin = if (pageOffset < 0) {
                        TransformOrigin(
                            1f,
                            0.5f
                        ) // Borde derecho si el cuadro va saliendo hacia la izquierda
                    } else {
                        TransformOrigin(
                            0f,
                            0.5f
                        ) // Borde izquierdo si el cuadro viene entrando desde la derecha
                    }

                    // Variable 5: rotationY (Ángulo de inclinación 3D)
                    rotationY = pageOffset * 90f

                    // Variable 6: cameraDistance (Distancia de la lente de la cámara virtual)
                    cameraDistance = 16 * density
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),//*************
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .drawWithContent {
                        drawContent() // Dibuja primero el lienzo original//*************

                        // Variable 7: alphaShadow (Intensidad de la penumbra al girar)
                        val pageOffset =
                            (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction
                        val alphaShadow = (abs(pageOffset) * 0.6f).coerceIn(0f, 0.7f)

                        // Dibuja el sombreado dinámico sobre la cara del cubo
                        drawRect(color = Color.Black.copy(alpha = alphaShadow))
                    }
            ) {
                // ⬇️ AGREGA ESTE BLOQUE AQUÍ ⬇️
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(cuadro.colorFondo),
                    contentAlignment = Alignment.BottomStart
                ) {
                    // Placa del museo en la parte inferior del cuadro
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.8f))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = cuadro.titulo,
                                color = Color(0xFFF5E6C8),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${cuadro.autor} (${cuadro.anio})",
                                color = Color.LightGray,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
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