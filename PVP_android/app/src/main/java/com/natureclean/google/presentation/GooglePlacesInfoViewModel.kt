package com.natureclean.google.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.google.GoogleBackend
import com.natureclean.api.model.Resource
import com.natureclean.formatGoogleWaypoints
import com.natureclean.google.domain.model.GeocodedWaypoints
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

import kotlinx.coroutines.launch
import javax.inject.Inject


const val MAP_KEY = "AIzaSyBNKkzRI4t5K3C9upmPNaAZ8OYN6VNjZT0"

@HiltViewModel
class GooglePlacesInfoViewModel @Inject constructor(private val googleApi: GoogleBackend) :
    ViewModel() {

    private val _googlePlacesInfoState = mutableStateOf(GooglePlacesInfoState())
    val googlePlacesInfoState: State<GooglePlacesInfoState> = _googlePlacesInfoState


    val polylines = mutableStateOf<List<LatLng>>(emptyList())
    fun getDirection(
        context: Context? = null,
        origin: String,
        destination: String,
        waypoints: List<LatLng>?,
        mode: String = "walking"
    ) {
        Log.e("my points", waypoints.toString())
        viewModelScope.launch {
            when (val response =
                googleApi.getDirections(
                    destination = destination,
                    origin = origin,
                    waypoints = waypoints?.formatGoogleWaypoints(),
                    mode = mode,
                )) {
                is Resource.Success -> {
                    _googlePlacesInfoState.value = googlePlacesInfoState.value.copy(
                        direction = response.data,
                        isLoading = false
                    )
                    googlePlacesInfoState.value.direction?.routes?.get(0)?.overview_polyline?.points?.let {
                        decoPoints(
                            points = it
                        )
                    }
                    Log.i("response suc", response.data.toString())

                }

                is Resource.Error -> {
                    Log.i("response e", response.toString())
                    Toast.makeText(context, "No better route is available", Toast.LENGTH_LONG).show()

                }

                else -> {
                    Log.i("response else", response.toString())

                }
            }
        }
    }

    private fun decoPoints(points: String) {
        polylines.value = decodePoly(points)
    }

    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }
}