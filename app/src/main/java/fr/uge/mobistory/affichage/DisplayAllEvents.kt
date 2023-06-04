package fr.uge.mobistory.affichage

import android.app.usage.EventStats
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.tri.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun displayAllEvents(
    eventRepository: EventRepository,
    sortType: SortType,
    showFavoritesOnly: Boolean,
    favoriteEvents: MutableList<HistoricalEventAndClaim>
) {
    val lazyListState = rememberLazyListState()
    var events: List<HistoricalEventAndClaim> by remember { mutableStateOf(listOf()) }


    LaunchedEffect(Unit) {
        events = eventRepository.getHistoricalEventWithClaimsAll()
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val filteredEvents = if (showFavoritesOnly) {
            favoriteEvents
        } else {
            events
        }
        val sortedEvents = when (sortType) {
                SortType.DATE -> sortByDate(filteredEvents)
                SortType.LABEL -> sortByLabel(filteredEvents)
                SortType.LOCATION -> sortByLocation(filteredEvents)
                SortType.POPULARITY -> sortByPopularity(filteredEvents)
            }

        items(sortedEvents) { event ->
            displayEvent(event = event, favoriteEvents)
        }
    }
}