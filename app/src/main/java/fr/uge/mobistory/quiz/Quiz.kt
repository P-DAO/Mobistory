package fr.uge.mobistory.quiz

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.utils.extractDatesFromClaims
import fr.uge.mobistory.utils.extractLabel
import java.util.Date

enum class QuizState {
    START,
    QUESTION1,
    QUESTION2,
    QUESTION3,
    QUESTION4,
    QUESTION5,
    END;

    companion object {
        fun changeState(currentState: QuizState): QuizState {
            return when(currentState) {
                START -> QUESTION1
                QUESTION1 -> QUESTION2
                QUESTION2 -> QUESTION3
                QUESTION3 -> QUESTION4
                QUESTION4 -> QUESTION5
                QUESTION5 -> END
                END -> START
            }
        }
    }
}

//private fun extractDateFromString(date: String): Date {
//
//}

//private fun minOfDate(event1: HistoricalEventAndClaim, event2: HistoricalEventAndClaim): Boolean {
//    val date1 = extractDatesFromClaims(event1.claims)
//    val date2 = extractDatesFromClaims(event2.claims)
//
//
//}

@Composable
fun Quiz(events: List<HistoricalEventAndClaim>) {
    var state by rememberSaveable { mutableStateOf(QuizState.START) }
    var point by rememberSaveable { mutableStateOf(0) }
    var answer by rememberSaveable { mutableStateOf("") }
    
    when (state) {
        QuizState.START -> {
            StartButton(state = { state = it } )
            point = 0
        }
        QuizState.END -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    if (point == 5)
                        Text(text = "You have $point/5 points", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    else
                        Text(text = "You have $point/5 points, the correct answer was $answer.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Text(text = "Do you want to retry ?", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(onClick = {
                            state = QuizState.QUESTION1
                            point = 0
                        }) { Text(text= "retry") }
                        Button(onClick = { state = QuizState.START }) { Text(text = "stop") }
                    }
                }
            }
        }
        else -> {
            DisplayRandomEvent(events = events, state = state, newState = {state = it}, point = {point += it}, answer = {answer = it})
        }
    }
}

@Composable
fun StartButton(state: (QuizState) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { state(QuizState.QUESTION1) }, modifier = Modifier.align(Alignment.Center)) {
            Text(text = "start the quiz")
        }
    }
}

@Composable
fun DisplayRandomEvent(events: List<HistoricalEventAndClaim>, state: QuizState, point: (Int) -> Unit, answer: (String) -> Unit, newState: (QuizState) -> Unit) {
    var event1 = events.random()
    var event2 = events.random()
    while (event2 == event1)
        event2 = events.random()

    val firstEvent = extractLabel(event1)!!
    val secondEvent = extractLabel(event2)!!

    Column(Modifier.fillMaxSize()) {
        Text(text = "Quelle évènement est le plus vieux ?", modifier = Modifier.fillMaxWidth().weight(1f/5f, true), textAlign = TextAlign.Center)
        Row(modifier = Modifier.fillMaxWidth().weight(4f/5f, true), horizontalArrangement = Arrangement.Center) {
            Box(Modifier.weight(1F/2F, true).background(color = Color.White, shape = RoundedCornerShape(10.dp))) {
                Text(text = firstEvent, color = Color.Black, textAlign = TextAlign.Center, modifier = Modifier.clickable(onClick = {
                    if ( true ) {
                        newState(QuizState.changeState(state))
                        point(1)
                    } else {
                        answer(secondEvent)
                        newState(QuizState.END)
                    }
                }))
            }
            Box(Modifier.weight(1F/2F, true).background(color = Color.White, shape = RoundedCornerShape(10.dp))) {
                Text(text = secondEvent, color = Color.Black, textAlign = TextAlign.Center, modifier = Modifier.clickable(onClick = {
                    if ( false ) {
                        newState(QuizState.changeState(state))
                        point(1)
                    } else {
                        answer(firstEvent)
                        newState(QuizState.END)
                    }
                }))
            }
        }
    }

}