package fr.uge.mobistory

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class ImportFile {

    suspend fun importEventsFromFile(context: Context, database: EventDatabase){
        val events = mutableListOf<HistoricalEvent>()
        val file = File(context.filesDir, "res/raw/events.txt")
        val reader = BufferedReader(FileReader(file))

        var line: String? = reader.readLine()
        while(line != null){
            val event = parseEventFromText(line)
            events.add(event)
            line = reader.readLine()
        }

        reader.close()

        val eventDao = database.historicalEventsDao()
        eventDao.insertEvents(events)
    }

    private fun parseEventFromText(text: String): HistoricalEvent{
        val historicalEvent = Json.parseToJsonElement(text)
//        val historicalEvent = Json.decodeFromString<List<HistoricalEvent>>(text)
        val id = historicalEvent.jsonObject["id"]?.jsonPrimitive?.int
        val label = historicalEvent.jsonObject["label"]?.jsonPrimitive?.content
        val aliases = historicalEvent.jsonObject["aliases"]?.jsonPrimitive?.content
        val description = historicalEvent.jsonObject["description"]?.jsonPrimitive?.content
        val popularity = historicalEvent.jsonObject["popularity"]?.jsonPrimitive?.int
        val claimsArray = historicalEvent.jsonObject["claims"]?.jsonArray

        val claims = mutableListOf<HistoricalEvent.Claims>()

        claimsArray?.forEach{
            claimArray ->
            val id = claimArray.jsonObject["id"]?.jsonPrimitive?.long
            val eventId = claimArray.jsonObject["eventId"]?.jsonPrimitive?.int
            val verbodeName = claimArray.jsonObject["verboseName"]?.jsonPrimitive?.content
            val value = claimArray.jsonObject["value"]?.jsonPrimitive?.content
            val item = claimArray.jsonObject["item"]?.jsonPrimitive?.content

            val claim = HistoricalEvent.Claims(id, eventId, verbodeName, value, item)
            claims.add(claim)

        }
        // Créez un objet HistoricalEvent en utilisant les valeurs extraites du JSON
        return HistoricalEvent(id, label, aliases, description, popularity, claims)
    }
}