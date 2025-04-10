package no.solcellepaneller.ui.electricity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import no.solcellepaneller.model.electricity.ElectricityPrice
import java.time.ZonedDateTime

@Composable
fun ElectricityPriceChart(prices: List<ElectricityPrice>) {
    var chartType by remember { mutableStateOf(ChartType.LINE) }

    val points = prices.map { price ->
        val hour = ZonedDateTime.parse(price.time_start).hour.toFloat()
        Point(hour, price.NOK_per_kWh.toFloat())
    }

    val bars = prices.map { price ->
        val hour = ZonedDateTime.parse(price.time_start).hour
        BarData(
            point = Point(hour.toFloat(), price.NOK_per_kWh.toFloat()),
            label = "$hour:00",
            color = when {
                price.NOK_per_kWh > 1.0 -> Color.Red
                price.NOK_per_kWh > 0.7 -> Color.Yellow
                else -> Color.Green
            }
        )
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .steps(prices.size - 1)
        .labelData { i -> "${i.toInt()}:00" }
        .axisLabelAngle(45f)
        .build()

    val maxPrice = prices.maxOf { it.NOK_per_kWh }
    val minPrice = prices.minOf { it.NOK_per_kWh }
    val yAxisData = AxisData.Builder()
        .topPadding(20.dp)
        .labelData { i -> "%.2f".format(i.toFloat()) }
        .axisStepSize(((maxPrice - minPrice) / 5).toFloat().dp)
        .build()

    Column {
        Button(
            onClick = {
                chartType = if (chartType == ChartType.LINE) ChartType.BAR else ChartType.LINE
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = if (chartType == ChartType.LINE) "Vis søylediagram" else "Vis linjediagram")
        }

        when (chartType) {
            ChartType.LINE -> {
                val lineChartData = LineChartData(
                    linePlotData = LinePlotData(
                        lines = listOf(
                            Line(
                                dataPoints = points,
                                LineStyle(color = Color.Blue),
                                IntersectionPoint(color = Color.Red),
                                SelectionHighlightPoint(color = Color.Yellow),
                                ShadowUnderLine(
                                    alpha = 0.5f,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Cyan,
                                            Color.Transparent
                                        )
                                    )
                                ),
                                SelectionHighlightPopUp()
                            )
                        ),
                    ),
                    xAxisData = xAxisData,
                    yAxisData = yAxisData,
                    gridLines = GridLines(color = Color.LightGray),
                    backgroundColor = Color.White
                )

                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp),
                    lineChartData = lineChartData
                )
            }

            ChartType.BAR -> {
                val barChartData = BarChartData(
                    chartData = bars,
                    xAxisData = xAxisData,
                    yAxisData = yAxisData,
                    barStyle = BarStyle(
                        paddingBetweenBars = 8.dp,
                        barWidth = 20.dp
                    ),
                    backgroundColor = Color.White
                )

                BarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp),
                    barChartData = barChartData
                )
            }
        }
    }
}
