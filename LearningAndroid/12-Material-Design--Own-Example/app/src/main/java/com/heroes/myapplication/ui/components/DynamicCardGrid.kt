package com.heroes.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heroes.myapplication.data.model.ItemDinamico
import kotlin.collections.chunked

@Composable
fun DynamicCardGrid(itemsList: List<ItemDinamico>, modifier: Modifier = Modifier) {

    val itemsParaRenderizar = generarItemsAgrupados(itemsList)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(itemsParaRenderizar.size) { index ->
            val elemento = itemsParaRenderizar[index]

            if (elemento.size > 1) {
                // Fila de 2 tarjetas chiquitas
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f)) { TarjetaCuadradaSimple(elemento[0]) }
                    Box(modifier = Modifier.weight(1f)) { TarjetaCuadradaSimple(elemento[1]) }
                }
            } else {
                // Fila de 1 tarjeta ancha
                val item = elemento[0]
                when (item.tipoTarjeta) {
                    1 -> TarjetaCuadradaSimple(item)
                    2 -> TarjetaImagenDerecha(item)
                    3 -> TarjetaPremiumImagenFondo(item)
                    4 -> TarjetaImagenIzquierda(item)
                }
            }
        }
    }
}

fun generarItemsAgrupados(lista: List<ItemDinamico>): List<List<ItemDinamico>> {
    val resultado = mutableListOf<List<ItemDinamico>>()
    val pendientesCuadradas = mutableListOf<ItemDinamico>()

    for (item in lista) {
        if (item.tipoTarjeta == 1) {
            pendientesCuadradas.add(item)
            if (pendientesCuadradas.size == 2) {
                resultado.add(pendientesCuadradas.toList())
                pendientesCuadradas.clear()
            }
        } else {
            if (pendientesCuadradas.isNotEmpty()) {
                resultado.add(pendientesCuadradas.toList())
                pendientesCuadradas.clear()
            }
            resultado.add(listOf(item))
        }
    }
    if (pendientesCuadradas.isNotEmpty()) {
        resultado.add(pendientesCuadradas.toList())
    }
    return resultado
}