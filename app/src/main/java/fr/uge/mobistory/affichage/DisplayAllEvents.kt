package fr.uge.mobistory.affichage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.database.HistoricalEventAndClaim
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun displayAllEvents(
    eventRepository: EventRepository,
    coroutineScope: CoroutineScope
) {
    val lazyListState = rememberLazyListState()
    val events = remember { mutableStateListOf<HistoricalEventAndClaim>() }

    LaunchedEffect(Unit) {
        loadNextEvents(eventRepository, events, coroutineScope)
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(events) { event ->
            displayEvent(event = event)
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { firstVisibleItem ->
                val totalItemCount = lazyListState.layoutInfo.totalItemsCount
                val lastVisibleItem =
                    firstVisibleItem + lazyListState.layoutInfo.visibleItemsInfo.size

                if (lastVisibleItem >= totalItemCount - 1) {
                    loadNextEvents(eventRepository, events, coroutineScope)
                }
            }
    }
}

fun loadNextEvents(
    eventRepository: EventRepository,
    events: MutableList<HistoricalEventAndClaim>,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        val offset = events.size
        val limit = 20 // Nombre d'événements à charger à chaque fois

        val nextEvents = eventRepository.getHistoricalEventWithClaims(offset, limit)
        events.addAll(nextEvents)
    }
}

