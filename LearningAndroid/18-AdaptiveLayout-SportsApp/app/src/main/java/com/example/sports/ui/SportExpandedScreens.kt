package com.example.sports.ui

import android.R.attr.onClick
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sports.R
import com.example.sports.data.LocalSportsDataProvider
import com.example.sports.model.Sport
import com.example.sports.ui.theme.SportsTheme

@Composable
fun SportsListAndDetails(
    sports: List<Sport>,
    onListItemClick: (Sport) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    selectedSport: Sport,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(modifier = modifier) {
        SportsList(
            sports = sports,
            onClick = onListItemClick,
            contentPadding = contentPadding,
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = dimensionResource(R.dimen.padding_small),    // <-- Agrega la separación debajo de la barra
                    start = dimensionResource(R.dimen.padding_medium),  // <-- Margen izquierdo
                    bottom = dimensionResource(R.dimen.padding_small)// <-- Margen derecho
                )
        )
        Spacer(modifier = Modifier.width(16.dp))
        SportsDetail(
            selectedSport = selectedSport,
            onBackPressed = onBackPressed,
            contentPadding = contentPadding,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun SportsListAndDetailsPreview() {
    SportsTheme {
        Surface {
            SportsListAndDetails(
                sports = LocalSportsDataProvider.getSportsData(),
                onListItemClick = {},
                selectedSport = LocalSportsDataProvider.defaultSport,
                onBackPressed = {},
                contentPadding = PaddingValues(0.dp)
            )
        }
    }
}
