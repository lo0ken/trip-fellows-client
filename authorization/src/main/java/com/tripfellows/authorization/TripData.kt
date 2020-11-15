
import java.sql.Time

data class TripData(
    val departureAddress: String,
    val destinationAddress: String,
    val startTime: Time,
    val places: Int,
    val price: String
)