package fr.uge.mobistory.affichage

import android.app.usage.EventStats
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
        items(events) { event ->
            displayEvent(event = event)
        }
    }
}