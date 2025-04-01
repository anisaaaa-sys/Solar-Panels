package no.solcellepaneller.ui.electricity

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.charts.line.*
import no.solcellepaneller.model.electricity.ElectricityPrice
import java.time.ZonedDateTime

@Composable
fun ElectricityPriceChart(prices: List<ElectricityPrice>) {
    val chartData = prices.map { price ->
        val hour = ZonedDateTime.parse(price.time_start).hour.toFloat()
        val priceValue = price.NOK_per_kWh.toFloat()
        LineChart.DataPoint(hour, priceValue)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LineChart(
            data = chartData,
            lineColor = Color.Blue,
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )
    }
}
