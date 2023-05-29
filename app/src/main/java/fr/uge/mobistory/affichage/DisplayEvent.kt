package fr.uge.mobistory.affichage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.HistoricalEventAndClaim

@Composable
fun displayEvent(event: HistoricalEventAndClaim){
    Row(
        modifier = Modifier.padding(16.dp)
    ){
//        Image()
    }
    Spacer(modifier = Modifier.padding(8.dp))
    Column {
        Text(
            text = event.historicalEvent.label,
            style = MaterialTheme.typography.h4,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = event.historicalEvent.description,
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Popularity: ${event.historicalEvent.popularity.fr}",
            style = MaterialTheme.typography.body2,
        )

    }
}