package fr.uge.mobistory

import fr.uge.mobistory.database.EventDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImportFile (private val eventDatabase: EventDatabase){

    suspend fun importEvents(events: List<HistoricalEvent>){
        withContext(Dispatchers.IO){
            eventDatabase.historicalEventDao().upsertEvents(events)
        }
    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    suspend fun importEventsFromFile(context: Context, database: EventDatabase) {
//        val events = mutableListOf<HistoricalEvent>()
//        val file = File(context.filesDir, "app/res/raw/events.txt")
//        val reader = BufferedReader(FileReader(file))
//
//        var line: String? = reader.readLine()
//        while (line != null) {
//            val event = parseEventFromText(line)
//            events.add(event)
//            line = reader.readLine()
//        }
//
//        reader.close()
//
//        val eventDao = database.historicalEventsDao()
//        eventDao.insertEvents(events)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun parseEventFromText(text: String): HistoricalEvent {
//        val historicalEvent = Json.parseToJsonElement(text)
////        val historicalEvent = Json.decodeFromString<List<HistoricalEvent>>(text)
//        val id = historicalEvent.jsonObject["id"]?.jsonPrimitive?.int ?: 0
//        val label = historicalEvent.jsonObject["label"]?.jsonPrimitive?.content
//        val aliases = historicalEvent.jsonObject["aliases"]?.jsonPrimitive?.content
//        val description = historicalEvent.jsonObject["description"]?.jsonPrimitive?.content
////        val date: Date? = parseDateFromDescription(description as String)
//        val popularity = historicalEvent.jsonObject["popularity"]?.jsonPrimitive?.int ?: 0
////        val location = historicalEvent.jsonObject["location"]?.jsonPrimitive?.content
//        val claimsArray = historicalEvent.jsonObject["claims"]?.jsonArray
//
//        val claims = mutableListOf<HistoricalEvent.Claims>()
//
//        if (claimsArray != null) {
//            claimsArray?.forEach { claimArray ->
//                val id = claimArray.jsonObject["id"]?.jsonPrimitive?.long ?: 0
//                val eventId = claimArray.jsonObject["eventId"]?.jsonPrimitive?.int
//                val verbodeName = claimArray.jsonObject["verboseName"]?.jsonPrimitive?.content
//                val value = claimArray.jsonObject["value"]?.jsonPrimitive?.content
//                val item = claimArray.jsonObject["item"]?.jsonPrimitive?.content
//
//                val claim = HistoricalEvent.Claims(id, eventId, verbodeName, value, item)
//                claims.add(claim)
//            }
//        }
//        return HistoricalEvent(id, label as String, aliases, description, /*date,*/ popularity, /*location,*/ claims.takeIf { claims.isNotEmpty() })
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun parseDateFromDescription(description: String): Date?{
//        val regex = Regex("\\b(\\d{1,2})\\s+(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)\\b(?:\\s+(\\d{4}))?")
//        val matchResult = regex.find(description)
//        val (day, month, year) = matchResult?.destructured ?: return null
//
//        val monthNumber = Month.valueOf(month.uppercase()).value
//        val localDate = if (year.isNotBlank()) {
//            LocalDate.of(year.toInt(), monthNumber, day.toInt())
//        } else {
//            LocalDate.of(0, monthNumber, day.toInt())
//        }
//
//        val zoneId: ZoneId = ZoneId.systemDefault()
//        return Date.from(localDate.atStartOfDay(zoneId).toInstant())
//    }
}