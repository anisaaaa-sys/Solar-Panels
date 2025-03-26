package no.solcellepaneller.model.electricity

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ElectricityPrice(
    val NOK_per_kWh: Double,
    val EUR_per_kWh: Double,
    val EXR: Double,
    val time_start: String,
    val time_end: String,
    val region: String = "",
    val date: String = LocalDate.now().toString()
) {
    fun getTimeRange(): String {
        return "$time_start - $time_end"
    }
}