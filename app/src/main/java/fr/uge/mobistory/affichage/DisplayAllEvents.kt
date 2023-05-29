package fr.uge.mobistory.affichage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.database.HistoricalEventAndClaim
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun loadNextEvents(
    eventRepository: EventRepository,
    toMutableList: MutableList<HistoricalEventAndClaim>,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        val nextEvents = eventRepository.getHistoricalEventWithClaims()
        toMutableList.addAll(nextEvents)
    }
}

@Composable
fun displayAllEvents(
    events: List<HistoricalEventAndClaim>,
    eventRepository: EventRepository,
    coroutineScope: CoroutineScope
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        events.forEach { event ->
            item {
                displayEvent(event = event)
            }
        }
    }
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { firstVisibleItem ->
                val totalItemCount = lazyListState.layoutInfo.totalItemsCount
                val lastVisibleItem =
                    firstVisibleItem + lazyListState.layoutInfo.visibleItemsInfo.size

                if (lastVisibleItem >= totalItemCount - 1) {
                    loadNextEvents(eventRepository, events.toMutableList(), coroutineScope)
                }
            }
    }
}
