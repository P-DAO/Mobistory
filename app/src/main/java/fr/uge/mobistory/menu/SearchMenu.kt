package fr.uge.mobistory.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.utils.extractLabel
import java.util.Locale

enum class SearchMenuState {
    OPEN,
    CLOSE,
}

@Composable
fun EventListItem(label: String, onItemClick: (String) -> Unit) {
    Text(
        modifier = Modifier
            .clickable( onClick = { onItemClick(label) } )
            .background(color = Color.White, shape = RoundedCornerShape(corner = CornerSize(10.dp)))
            .fillMaxWidth()
            .padding(5.dp),
        text = label,
        color = Color.Black
    )
}

@Composable
fun EventList(listEvent: List<HistoricalEventAndClaim>, state: MutableState<TextFieldValue>, event: (String) -> Unit, searchState: (SearchMenuState) -> Unit) {
    var filteredEvent: ArrayList<String>
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        val searchedText = state.value.text
        val resultList = ArrayList<String>()
        for (_event in listEvent) {
            val label = extractLabel(_event)
            if (label!!.lowercase(Locale.getDefault()).contains(searchedText.lowercase(Locale.getDefault())))
                resultList.add(label)
        }
        filteredEvent = resultList
        items(filteredEvent) { filteredEvent ->
            EventListItem(label = filteredEvent, onItemClick = { selectedEvent ->
                event(selectedEvent)
                searchState(SearchMenuState.CLOSE)
            })
        }
    }
}