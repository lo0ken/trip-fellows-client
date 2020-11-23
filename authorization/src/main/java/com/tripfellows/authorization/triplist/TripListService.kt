package com.tripfellows.authorization.triplist

import com.tripfellows.authorization.model.Trip
import java.sql.Time
import java.util.*

object TripListService {

    fun tripList() : List<Trip> {
        val cities: Array<String> = arrayOf(
            "Moscow",
            "Mytishchi",
            "Lyubertcty",
            "Pushkino",
            "Ivanteyevka",
            "Reutov",
            "Korolyov",
            "Elektrostal"
        )

        val data = mutableListOf<Trip>()

        val random = Random()
        val millisInDay = 24 * 60 * 60 * 1000

        for (i in 0..50) {
            val trip = Trip(
                i,
                cities[random.nextInt(cities.size)],
                cities[random.nextInt(cities.size)],
                Time(random.nextInt(millisInDay).toLong()),
                random.nextInt(1..5),
                random.nextInt(100..1000).toString() + "p"
            )
            data.add(trip)
        }
        return Collections.unmodifiableList(data)
    }
}

fun Random.nextInt(range: IntRange): Int {
    return range.first + nextInt(range.last - range.first)
}