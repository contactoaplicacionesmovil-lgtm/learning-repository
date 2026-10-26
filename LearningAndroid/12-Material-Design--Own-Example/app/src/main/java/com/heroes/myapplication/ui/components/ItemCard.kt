package com.heroes.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.heroes.myapplication.data.model.ItemDinamico
import com.heroes.myapplication.R


// ==========================================
// TARJETA 1: Imagen a la derecha (pegada top y bottom)
// ==========================================
@Composable
fun TarjetaImagenDerecha(item: ItemDinamico) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Column(modifier = Modifier.weight(1f).padding(12.dp)) {
                Text(text = item.titulo, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(text = item.descripcion, style = MaterialTheme.typography.bodySmall)
            }
            Image(
                painter = painterResource(id = item.imagenId), // Dinámico
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight().aspectRatio(1.5f)
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
            )
        }
    }
}

@Composable
fun TarjetaImagenIzquierda(item: ItemDinamico) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Image(
                painter = painterResource(id = item.imagenId), // Dinámico
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight().aspectRatio(1.5f)
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            )
            Column(modifier = Modifier.weight(1f).padding(12.dp)) {
                Text(text = item.titulo, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(text = item.descripcion, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun TarjetaPremiumImagenFondo(item: ItemDinamico) {
    Card(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                Image(
                    painter = painterResource(id = item.imagenId), // Dinámico
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier.fillMaxSize().background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 100f
                    )
                )
            )
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(text = item.titulo, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.descripcion, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            }
        }
    }
}

@Composable
fun TarjetaCuadradaSimple(item: ItemDinamico) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f), // Mantiene la tarjeta cuadrada
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // El Box nos permite poner cosas encima de otras
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. TEXTOS (Arriba a la izquierda)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    // OJO AQUÍ: Padding derecho para que el texto no choque con la imagen
                    .padding(end = 40.dp)
            ) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3 // Para que no desborde si es muy largo
                )
            }

            // 2. IMAGEN (Esquina inferior derecha)
            Image(
                painter = painterResource(id = item.imagenId),
                contentDescription = null,
                contentScale = ContentScale.Fit, // Fit para que se vea entera
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Pegada abajo a la derecha
                    .size(70.dp) // AQUI ESTA EL CAMBIO: Tamaño fijo, chiquita
                    .padding(8.dp) // Un pequeño respiro del borde
            )
        }
    }
}

