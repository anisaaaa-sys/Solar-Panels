package no.solcellepanelerApp.data.mapdata

import no.solcellepanelerApp.model.map.GeocodingResponse

<<<<<<< HEAD
class AddressRepository(
    private val dataSource: AddressDataSource,
    private val elevationApi: ElevationApi
) {
=======
class AddressRepository(private val dataSource: AdressDataSource) {
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

    suspend fun getCoordinates(address: String): List<GeocodingResponse> {
        return dataSource.getCoordinates(address)
    }

<<<<<<< HEAD
    suspend fun getHeight(coordinates: Pair<Double,Double>): Double? {
        return elevationApi.fetchElevation(coordinates)
    }
=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
}

