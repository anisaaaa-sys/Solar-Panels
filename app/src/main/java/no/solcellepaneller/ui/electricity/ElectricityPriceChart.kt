package no.solcellepaneller.ui.electricity

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import no.solcellepaneller.model.electricity.ElectricityPrice
import java.time.ZonedDateTime

@Composable
fun ElectricityPriceChart(prices: List<ElectricityPrice>) {
    val points = prices.mapIndexed { index, price ->
        val hour = ZonedDateTime.parse(price.time_start).hour.toFloat()
        Point(hour.toFloat(), price.NOK_per_kWh.toFloat())
    }

    // Prepare X-axis (hours)
    val xAxisData = AxisData.Builder()
        .axisStepSize(2.dp)
        .steps(points.size - 1)
        .labelData { i -> "${i.toInt()}:00" }
        .axisLabelAngle(0f)
        .build()

    // Prepare Y-axis (prices)
    val maxPrice = prices.maxOf { it.NOK_per_kWh }
    val minPrice = prices.minOf { it.NOK_per_kWh }
    val yAxisData = AxisData.Builder()
        .topPadding(20.dp)
        .labelData { i -> "%.2f".format(i.toDouble()) }
        .axisStepSize(((maxPrice - minPrice).toFloat() / 5).dp)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    LineStyle(color = androidx.compose.ui.graphics.Color.Blue),
                    IntersectionPoint(color = androidx.compose.ui.graphics.Color.Red),
                    SelectionHighlightPoint(color = androidx.compose.ui.graphics.Color.Yellow),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                androidx.compose.ui.graphics.Color.Cyan,
                                androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = androidx.compose.ui.graphics.Color.LightGray),
        backgroundColor = androidx.compose.ui.graphics.Color.White
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        lineChartData = lineChartData
    )
}