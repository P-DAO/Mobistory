package fr.uge.mobistory.quiz

import android.os.SystemClock
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.utils.extractDatesFromClaims
import fr.uge.mobistory.utils.extractLabel
import kotlinx.coroutines.delay
import java.time.LocalDate
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

private enum class ButtonState {
    ON,
    OFF
}

private fun extractDateFromString(date: String, regex1: Regex, regex2: Regex, regex3: Regex, regex4: Regex, regex5: Regex): LocalDate {
    var newDate = ""
    if (date.matches(regex1)) {
        newDate = date.removePrefix("de ")
        newDate = newDate.removeSuffix(" à [0-9]*")
        return LocalDate.of(newDate.toInt(), 1, 1)
    }
    if (date.matches(regex2)) {
        newDate = date.removeSuffix(" av. J-C")
        return LocalDate.of(newDate.toInt(), 1, 1)
    }
    if (date.matches(regex3)) {
        return LocalDate.of(date.toInt(), 1, 1)
    }
    if (date.matches(regex4)) {
        var list = date.split("/")
        return LocalDate.of(list[2].toInt(), list[1].toInt(), list[0].toInt())
    }
    if (date.matches(regex5)) {
        newDate = date.removePrefix("de ")
        newDate = newDate.removeSuffix(" à [0-9]*\\/[0-9]*\\/[0-9]*")
        var list = newDate.split("/")
        return LocalDate.of(list[2].toInt(), list[1].toInt(), list[0].toInt())
    }
    return LocalDate.of(0, 0, 0)
}

private fun minOfDate(event1: HistoricalEventAndClaim, event2: HistoricalEventAndClaim, regex1: Regex, regex2: Regex, regex3: Regex, regex4: Regex, regex5: Regex): Boolean {
    val date1 = extractDatesFromClaims(event1.claims)!!
    val date2 = extractDatesFromClaims(event2.claims)!!

    val firstDate = extractDateFromString(date1, regex1, regex2, regex3, regex4, regex5)
    val secondDate = extractDateFromString(date2, regex1, regex2, regex3, regex4, regex5)

    return firstDate < secondDate
}

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

    var buttonState by rememberSaveable { mutableStateOf(ButtonState.OFF) }
    var color1 by remember { mutableStateOf(Color.White) }
    var color2 by remember { mutableStateOf(Color.White) }

    val regex1 = Regex("^de [0-9]* à [0-9]*\$")
    val regex2 = Regex("^-[0-9]* av. J-C\$")
    val regex3 = Regex("^[0-9]*\$")
    val regex4 = Regex("^[0-9]*\\/[0-9]*\\/[0-9]*\$")
    val regex5 = Regex("^de [0-9]*\\/[0-9]*\\/[0-9]* à [0-9]*\\/[0-9]*\\/[0-9]*\$")

    val treatedEvents = events.filter {
        regex1.matches(extractDatesFromClaims(it.claims)!!) ||
        regex2.matches(extractDatesFromClaims(it.claims)!!) ||
        regex3.matches(extractDatesFromClaims(it.claims)!!) ||
        regex4.matches(extractDatesFromClaims(it.claims)!!) ||
        regex5.matches(extractDatesFromClaims(it.claims)!!)
    }

    var event1 = treatedEvents.random()
    var event2 = treatedEvents.random()
    while (event2 == event1)
        event2 = treatedEvents.random()

    val firstEvent = extractLabel(event1)!!
    val secondEvent = extractLabel(event2)!!

    if (buttonState == ButtonState.ON) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally) {
            Text(text= "well done, click to continue")
            Button(onClick = {
                newState(QuizState.changeState(state))
                color2 = Color.White
                color1 = Color.White
                buttonState = ButtonState.OFF
            }) {
                Text(text = "Continue")
            }
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxWidth().weight(2f / 5f, true), contentAlignment = Alignment.Center) {
                Text(
                    text = "Quelle évènement est le plus vieux ?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(3f / 5f, true), horizontalArrangement = Arrangement.Center) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(1F / 2F, true), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = firstEvent,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(color1, RoundedCornerShape(10.dp))
                            .clickable(onClick = {
                                buttonState = ButtonState.ON
                                if (minOfDate(
                                        event1,
                                        event2,
                                        regex1,
                                        regex2,
                                        regex3,
                                        regex4,
                                        regex5
                                    )
                                ) {
                                    point(1)
                                    color1 = Color.Green
                                } else {
                                    answer(secondEvent)
                                    newState(QuizState.END)
                                }
                            })
                    )
                }
                Box(Modifier.fillMaxHeight().weight(1F/2F, true), contentAlignment = Alignment.Center) {
                    Text(
                        text = secondEvent,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(color2, RoundedCornerShape(10.dp))
                            .clickable(onClick = {
                                buttonState = ButtonState.ON
                                if (minOfDate(
                                        event2,
                                        event1,
                                        regex1,
                                        regex2,
                                        regex3,
                                        regex4,
                                        regex5
                                    )
                                ) {
                                    point(1)
                                    color2 = Color.Green
                                } else {
                                    answer(firstEvent)
                                    newState(QuizState.END)
                                }
                            })
                    )
                }
            }
        }
    }
}