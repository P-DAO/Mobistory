package fr.uge.mobistory.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.HistoricalEvent
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.popularity.Popularity
import fr.uge.mobistory.utils.extractDateFromValue

enum class QuizState {
    START,
    QUESTION,
    END;
}

@Composable
fun Quiz(events: List<HistoricalEventAndClaim>) {
    val events: ArrayList<HistoricalEventAndClaim> = arrayListOf()
    var currentEvent by rememberSaveable { mutableStateOf(events.first().historicalEvent) }
    var state by rememberSaveable { mutableStateOf(QuizState.START) }
    var point by rememberSaveable { mutableStateOf(0) }
    
    when (state) {
        QuizState.START -> { startButton(state = { state = it } ) }
        QuizState.QUESTION -> {
            var event1 = events.random().historicalEvent
            var event2 = events.random().historicalEvent
            while (event2 == event1)
                event2 = events.random().historicalEvent
            //TODO extraction des dates
//            var answer = minOf(date1, date2)
            displayRandomEvent(event1 = event1, event2 = event2, selectedEvent = {currentEvent = it})
        }
        QuizState.END -> {}
    }
}

@Composable
fun startButton(state: (QuizState) -> Unit) {
    Button(onClick = { state(QuizState.QUESTION) }) {
        Text(text = "start the quiz")
    }
}

@Composable
fun displayRandomEvent(event1: HistoricalEventEntity, event2: HistoricalEventEntity, selectedEvent: (HistoricalEventEntity) -> Unit) {
    Row {
        Text(text = "${event1.label}", modifier = Modifier.clickable(onClick = {
            selectedEvent(event1)
        }))
        Text(text = "${event2.label}", modifier = Modifier.clickable(onClick = {
            selectedEvent(event2)
        }))
    }
}