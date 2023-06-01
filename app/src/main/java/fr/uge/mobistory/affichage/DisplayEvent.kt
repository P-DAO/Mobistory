package fr.uge.mobistory.affichage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.mobistory.dao.HistoricalEventsDao
import fr.uge.mobistory.database.EventDatabase
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.utils.*

@Composable
fun displayEvent(event: HistoricalEventAndClaim) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
//            Image(painter = , contentDescription = )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "${extractLabel(event)}",
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,

                    )
                Text(
                    text = "Description:\n ${extractDescription(event)}",
                    color = Color.Black,
                    fontSize = 15.sp
                )
                Text(
                    text = "Popularity: ${event.historicalEvent.popularity.fr}",
                    fontSize = 15.sp,
                    color = Color.Black,
                    )
                // TODO créer une condition si date n'existe pas
                Text(
                    text = "Date : ${extractDatesFromClaims(event.claims)}",
                    fontSize = 15.sp,
                    color = Color.Black,
                    )
                // TODO créer une condition si geo n'existe pas
                val (latitude, longitude) = extractGeoLatitudeLongiture(extractGeo(event.claims))
                Text(
                    text = "Coordinate :\n Latitude: ${latitude}\n Longitude: ${longitude}",
                    fontSize = 15.sp,
                    color = Color.Black,
                    )
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                var isFavorite by remember { mutableStateOf(event.historicalEvent.isFavorite) }
                val iconTint = if(isFavorite) Color.Yellow else Color.Gray

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favori",
                    tint = iconTint,
                    modifier = Modifier.size(24.dp).clickable {
                        isFavorite = !isFavorite
                        event.historicalEvent.isFavorite = !event.historicalEvent.isFavorite
                        EventDatabase.getInstance(context).historicalEventDao().updateEvent(event.historicalEvent)
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
}