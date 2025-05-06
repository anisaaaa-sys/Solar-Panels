package no.solcellepanelerApp.data.weatherdata

<<<<<<< HEAD
import androidx.compose.runtime.Composable
import no.solcellepanelerApp.ui.handling.AuthorizationErrorScreen
import no.solcellepanelerApp.ui.handling.NetworkErrorScreen
import no.solcellepanelerApp.ui.handling.OverloadErrorScreen
import no.solcellepanelerApp.ui.handling.RequestErrorScreen
import no.solcellepanelerApp.ui.handling.SeaErrorScreen
import no.solcellepanelerApp.ui.handling.ServerErrorScreen
import no.solcellepanelerApp.ui.handling.TimeoutErrorScreen
import no.solcellepanelerApp.ui.handling.UnknownErrorScreen

=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
enum class ApiError {
    TIMEOUT_ERROR,
    AUTHORIZATION_ERROR,
    SERVER_ERROR,
    OVERLOAD_ERROR,
    NETWORK_ERROR,
<<<<<<< HEAD
    UNKNOWN_ERROR,
    REQUEST_ERROR,
    SEA_ERROR
}

class ApiException(val errorCode: ApiError) : Throwable() {
    fun getErrorScreen(): @Composable () -> Unit {
        return when (errorCode) {
            ApiError.TIMEOUT_ERROR -> { { TimeoutErrorScreen() } }
            ApiError.AUTHORIZATION_ERROR -> { { AuthorizationErrorScreen() } }
            ApiError.SERVER_ERROR -> { { ServerErrorScreen() } }
            ApiError.OVERLOAD_ERROR -> { { OverloadErrorScreen() } }
            ApiError.NETWORK_ERROR -> { { NetworkErrorScreen() } }
            ApiError.UNKNOWN_ERROR -> { { UnknownErrorScreen() } }
            ApiError.REQUEST_ERROR -> { { RequestErrorScreen() } }
            ApiError.SEA_ERROR -> { { SeaErrorScreen() } }
=======
    UNKNOWN_ERROR
}

class ApiException(val errorCode: ApiError) : Throwable() {
    override fun toString(): String {
        return when (errorCode) {
            ApiError.TIMEOUT_ERROR -> "Request timed out. Please check your internet connection and try again."
            ApiError.AUTHORIZATION_ERROR -> "API authorization failed. Please report to developers."
            ApiError.SERVER_ERROR -> "Server error. Please try again later."
            ApiError.OVERLOAD_ERROR -> "An API key has reached rate-limit. Please wait and retry."
            ApiError.NETWORK_ERROR -> "Could not connect to domain. Please check your internet connection or if the domain is down."
            ApiError.UNKNOWN_ERROR -> "An unknown error occurred. Please report how you achieved this to the developers."
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
        }
    }
}
