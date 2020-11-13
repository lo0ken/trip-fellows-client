package com.tripfellows.client

import java.sql.Time
import java.util.*

object TripListService {

    fun departureList() : List<TripData> {
        val cities: Array<String> = arrayOf(
            "Moscow",
            "Mitishi",
            "Lyberci"
        )
        val town: Array<String> = arrayOf(
            "Pushkino",
            "Monino",
            "Korolev"
        )

        val data = mutableListOf<TripData>()

        val random = Random()
        val millisInDay = 24 * 60 * 60 * 1000
        val time = Time(random.nextInt(millisInDay).toLong())
        
        for (i in 0..50) {
            val trip = TripData(
                cities[random.nextInt(cities.size)],
                town[random.nextInt(town.size)],
                time,
                random.nextInt(1..5),
                random.nextInt(1000).toString() + "p"
            )
            data.add(trip)
        }
        return Collections.unmodifiableList(data)
    }
}

fun Random.nextInt(range: IntRange): Int {
    return range.start + nextInt(range.last - range.start)
}



