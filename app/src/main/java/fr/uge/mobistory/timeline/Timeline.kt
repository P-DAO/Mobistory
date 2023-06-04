package fr.uge.mobistory.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.utils.extractDatesFromClaims
import fr.uge.mobistory.utils.extractLabel
import java.time.LocalDate

private enum class Expression(val regex: Regex) {
    EXPR1(Regex("^de [0-9]* à [0-9]*\$")),
    EXPR2(Regex("^-[0-9]* av. J-C\$")),
    EXPR3(Regex("^[0-9]*\$")),
    EXPR4(Regex("^[0-9]*\\/[0-9]*\\/[0-9]*\$")),
    EXPR5(Regex("^de [0-9]*\\/[0-9]*\\/[0-9]* à [0-9]*\\/[0-9]*\\/[0-9]*\$"))
}

private fun extractDateFromString(date: String): LocalDate {
    var newDate = ""
    if (date.matches(Expression.EXPR1.regex)) {
        newDate = date.removePrefix("de ")
        newDate = newDate.removeSuffix(" à [0-9]*")
        return LocalDate.of(newDate.toInt(), 1, 1)
    }
    if (date.matches(Expression.EXPR2.regex)) {
        newDate = date.removeSuffix(" av. J-C")
        return LocalDate.of(newDate.toInt(), 1, 1)
    }
    if (date.matches(Expression.EXPR3.regex)) {
        return LocalDate.of(date.toInt(), 1, 1)
    }
    if (date.matches(Expression.EXPR4.regex)) {
        var list = date.split("/")
        return LocalDate.of(list[2].toInt(), list[1].toInt(), list[0].toInt())
    }
    if (date.matches(Expression.EXPR5.regex)) {
        newDate = date.removePrefix("de ")
        newDate = newDate.removeSuffix(" à [0-9]*\\/[0-9]*\\/[0-9]*")
        var list = newDate.split("/")
        return LocalDate.of(list[2].toInt(), list[1].toInt(), list[0].toInt())
    }
    return LocalDate.of(0, 0, 0)
}

private fun newAddAll(events1: ArrayList<HistoricalEventAndClaim>, events2: ArrayList<HistoricalEventAndClaim>): ArrayList<HistoricalEventAndClaim> {
    events1.addAll(events2)
    return events1
}

@Composable
private fun DisplayEvent(event: HistoricalEventAndClaim) {
    val label = extractLabel(event)!!
    val date = extractDatesFromClaims(event.claims)!!

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, textAlign = TextAlign.Center, color = Color.Black)
        Text(text = date, textAlign = TextAlign.Center, color = Color.Black)
    }
}

@Composable
fun TimelineDisplayer(events: List<HistoricalEventAndClaim>) {

    val dateEvent = HashMap<LocalDate, ArrayList<HistoricalEventAndClaim>>()

    var treatedEvents = events.filter {
        Expression.EXPR1.regex.matches(extractDatesFromClaims(it.claims)!!) ||
        Expression.EXPR2.regex.matches(extractDatesFromClaims(it.claims)!!) ||
        Expression.EXPR3.regex.matches(extractDatesFromClaims(it.claims)!!) ||
        Expression.EXPR4.regex.matches(extractDatesFromClaims(it.claims)!!) ||
        Expression.EXPR5.regex.matches(extractDatesFromClaims(it.claims)!!)
    }

    treatedEvents = treatedEvents.sortedBy { event -> extractDateFromString(extractDatesFromClaims(event.claims)!!)  }

    for (event in treatedEvents) {
        dateEvent.merge(extractDateFromString(extractDatesFromClaims(event.claims)!!), arrayListOf(event)) { currentList, newList ->
            newAddAll(currentList, newList)
        }
    }

    val listDate = dateEvent.keys.toList().sorted()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(listDate) { date ->
            Row(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
                    .background(color = Color.White)
                    .drawBehind { drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    ) })
                {
                    Text(text = date.toString(), textAlign = TextAlign.Center, color = Color.Black)
                }
                LazyRow(modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)) {
                    items(dateEvent[date]!!) {event ->
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .width(200.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))) {
                            DisplayEvent(event = event)
                        }
                    }
                }
            }
        }
    }


//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(treatedEvents) {event ->
//            LazyRow(modifier = Modifier.fillMaxWidth()) {
//                items() {timeEvent ->
//
//                }
//            }
//        }
//    }
}