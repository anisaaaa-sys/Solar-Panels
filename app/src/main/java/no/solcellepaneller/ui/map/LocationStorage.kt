package no.solcellepaneller.ui.map

import android.content.Context
import android.location.Location

object LocationStorage {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_LAT = "last_lat"
    private const val KEY_LON = "last_lon"

    fun saveLocation(context: Context, location: Location) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putFloat(KEY_LAT, location.latitude.toFloat())
            putFloat(KEY_LON, location.longitude.toFloat())
            apply()
        }
    }

    fun loadLocation(context: Context): Location? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lat = prefs.getFloat(KEY_LAT, Float.MIN_VALUE)
        val lon = prefs.getFloat(KEY_LON, Float.MIN_VALUE)

        return if (lat != Float.MIN_VALUE && lon != Float.MIN_VALUE) {
            Location("").apply {
                latitude = lat.toDouble()
                longitude = lon.toDouble()
            }
        } else {
            null
        }
    }
}
