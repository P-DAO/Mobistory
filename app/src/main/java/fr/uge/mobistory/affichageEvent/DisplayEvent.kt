package fr.uge.mobistory.affichageEvent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.utils.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun displayEvent(event: HistoricalEventAndClaim, favoriteEvents: MutableList<HistoricalEventAndClaim>) {

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),

        ) {
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
                if (extractDescription(event) != null) {
                    Text(
                        text = "Description:\n ${extractDescription(event)}",
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                }
                if (event.historicalEvent.popularity.fr != -1) {
                    Text(
                        text = "Popularité: ${event.historicalEvent.popularity.fr}",
                        fontSize = 15.sp,
                        color = Color.Black,
                    )
                }
                Text(
                    text = "Date : ${extractDatesFromClaims(event.claims)}",
                    fontSize = 15.sp,
                    color = Color.Black,
                )
                val (latitude, longitude) = extractGeoLatitudeLongiture(extractGeo(event.claims))
                if (latitude != null && longitude != null) {
                    Text(
                        text = "Coordinate :\n Latitude: ${latitude}\n Longitude: ${longitude}",
                        fontSize = 15.sp,
                        color = Color.Black,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {

                Icon(
                    imageVector = if (event in favoriteEvents) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (event in favoriteEvents) "Favorite" else "Unfavorite",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (event in favoriteEvents) {
                                favoriteEvents.remove(event)
                            } else {
                                favoriteEvents.add(event)
                            }
                        }
                )
            }
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
}