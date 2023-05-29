package fr.uge.mobistory.affichage

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.mobistory.database.HistoricalEventAndClaim

@Composable
fun displayEvent(event: HistoricalEventAndClaim){
    Row(
        modifier = Modifier.padding(16.dp)
    ){
//        Image()
    }
    Spacer(modifier = Modifier.padding(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
            Column(
        modifier = Modifier.weight(1f)
        ) {
            Text(
                text = event.historicalEvent.label,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
//            Text(
//                text = event.historicalEvent.description,
//                fontSize = 20.sp
//            )
            Text(
                text = "Popularity: ${event.historicalEvent.popularity.fr}",
                fontSize = 20.sp
            )
//            Text(
//                text = "Claims: ${event.claims}",
//                fontSize = 20.sp
//            )

        }
    }
}