package no.solcellepanelerApp.ui.result

<<<<<<< HEAD
import androidx.compose.runtime.Composable
=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
<<<<<<< HEAD
import no.solcellepanelerApp.data.weatherdata.ApiException
import no.solcellepanelerApp.data.weatherdata.WeatherRepository
import no.solcellepanelerApp.ui.handling.NoDataErrorScreen
import no.solcellepanelerApp.ui.handling.PartialDataErrorScreen
import no.solcellepanelerApp.ui.handling.UnexpectedErrorScreen
import no.solcellepanelerApp.ui.handling.UnknownErrorScreen
=======
import no.solcellepanelerApp.data.weatherdata.WeatherRepository
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

enum class UiState {
    LOADING, SUCCESS, ERROR
}
<<<<<<< HEAD
data class MonthlyCalculationResult(
    val adjustedRadiation: List<Double>,
    val monthlyEnergyOutput: List<Double>,
    val monthlyPowerOutput: List<Double>,
    val yearlyEnergyOutput: Double
)
=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository(),
) : ViewModel() {
    private val _weatherData = MutableStateFlow<Map<String, Array<Double>>>(emptyMap())
    val weatherData: StateFlow<Map<String, Array<Double>>> = _weatherData
    private val _uiState = MutableStateFlow(UiState.LOADING)
    val uiState: StateFlow<UiState> = _uiState
<<<<<<< HEAD
    private val _errorScreen = MutableStateFlow<@Composable () -> Unit> { UnknownErrorScreen() }
    val errorScreen: StateFlow<@Composable () -> Unit> = _errorScreen
=======
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

    private val _frostDataRim = MutableStateFlow<Array<Double>>(emptyArray())
    val frostDataRim: StateFlow<Array<Double>> = _frostDataRim

<<<<<<< HEAD
    private val _calculationResults = MutableStateFlow<MonthlyCalculationResult?>(null)

    val calculationResults: StateFlow<MonthlyCalculationResult?> = _calculationResults

    // Default temperature coefficient for solar panels
    private val temperatureCoefficient = -0.44

    private val daysInMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
=======
    //private val lastWeatherData: Map<String, Array<Double>>? = null
    //fun getLastWeatherData(): Map<String, Array<Double>>? = lastWeatherData
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

    fun loadWeatherData(
        lat: Double,
        lon: Double,
<<<<<<< HEAD
        height: Double?,
=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
        slope: Int,
        azimuth: Int,
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.LOADING
<<<<<<< HEAD
            val result = repository.getPanelWeatherData(lat, lon, height, slope, azimuth)
            if (result.isSuccess) {
                _weatherData.value = result.getOrNull()?: emptyMap()
                if (_weatherData.value.isEmpty()) {
                    _errorScreen.value = (result.exceptionOrNull() as? ApiException)?.getErrorScreen()?: { NoDataErrorScreen() }
                    _uiState.value = UiState.ERROR
                } else if (_weatherData.value.size != 4) {
                    _errorScreen.value = (result.exceptionOrNull() as? ApiException)?.getErrorScreen()?: { PartialDataErrorScreen() }
=======
            val result = repository.getPanelWeatherData(lat, lon, slope, azimuth)
            if (result.isSuccess) {
                _weatherData.value = result.getOrNull()?: emptyMap()
                if (_weatherData.value.isEmpty()) {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Empty error"
                    _uiState.value = UiState.ERROR
                } else if (_weatherData.value.size != 4) {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Some elements missing. Guesstimation not implemented."
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
                    _uiState.value = UiState.ERROR
                } else {
                    _uiState.value = UiState.SUCCESS
                }
            } else {
                _uiState.value = UiState.ERROR
<<<<<<< HEAD
                println(result.exceptionOrNull())
                _errorScreen.value = (result.exceptionOrNull() as? ApiException)?.getErrorScreen()?: { UnexpectedErrorScreen() }
=======
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unexpected behavior"
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
            }
        }
    }

<<<<<<< HEAD
    fun fetchRimData(lat: Double, lon: Double, elements: String) {
=======
    fun fetchRimData(lat: Double, lon: Double, elements: String) { // PRODUCTION: Set to private
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
        viewModelScope.launch {
            _uiState.value = UiState.LOADING
            _frostDataRim.value = repository.getRimData(lat, lon, elements)
            _uiState.value = UiState.SUCCESS
        }
    }
<<<<<<< HEAD

    fun calculateSolarPanelOutput(panelArea: Double, efficiency: Double) {
        viewModelScope.launch {
            if (_weatherData.value.size != 4) {
                _errorScreen.value = { PartialDataErrorScreen() }
                _uiState.value = UiState.ERROR
                return@launch
            }

            val snowCoverData = _weatherData.value["mean(snow_coverage_type P1M)"] ?: emptyArray()
            val airTempData = _weatherData.value["mean(air_temperature P1M)"] ?: emptyArray()
            val cloudCoverData = _weatherData.value["mean(cloud_area_fraction P1M)"] ?: emptyArray()
            val radiationData = _weatherData.value["mean(PVGIS_radiation P1M)"] ?: emptyArray()

            // Process data and calculate energy output
            val adjustedRadiation = mutableListOf<Double>()
            val monthlyEnergyOutput = radiationData.indices.map { month ->
                adjustedRadiation.add(
                    radiationData[month] *
                            (1 - (cloudCoverData[month].coerceIn(0.0, 8.0) / 8)) *
                            (1 - (snowCoverData[month].coerceIn(0.0, 4.0) / 4))
                )
                val tempFactor = 1 + temperatureCoefficient * (airTempData[month] - 25)
                adjustedRadiation[month] * panelArea * (efficiency / 100.0) * tempFactor
            }

            // Calculate monthly power output (kW)
            val monthlyPowerOutput = monthlyEnergyOutput.mapIndexed { index, energyKWh ->
                val totalHours = daysInMonth[index] * 24 // Total hours in the month
                energyKWh / totalHours // Convert kWh to kW
            }

            // Calculate yearly total energy output
            val yearlyEnergyOutput = monthlyEnergyOutput.sum()

            _calculationResults.value = MonthlyCalculationResult(
                adjustedRadiation = adjustedRadiation,
                monthlyEnergyOutput = monthlyEnergyOutput,
                monthlyPowerOutput = monthlyPowerOutput,
                yearlyEnergyOutput = yearlyEnergyOutput
            )
        }
    }
}
=======
}
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
