package fr.uge.mobistory.affichage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import fr.uge.mobistory.database.HistoricalEventAndClaim

@Composable
fun displayAllEvents(events: List<HistoricalEventAndClaim>){
    LazyColumn {
        events.forEach{
            event ->
            item{
                displayEvent(event = event)
            }
        }
    }
}