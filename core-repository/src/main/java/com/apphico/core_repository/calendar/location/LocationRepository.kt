package com.apphico.core_repository.calendar.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Location
import com.apphico.core_model.toLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

interface LocationRepository {

    fun getLastKnownLocation(): Flow<Location?>

    fun getMyLocationCoordinates(): Flow<Location?>

    fun getMyLocationFullAddress(): Flow<Location?>

    fun getFromCoordinates(coordinates: Coordinates): Flow<Location?>

    fun getFromName(name: String): Flow<Location?>
}

class LocationRepositoryImpl(val context: Context) : LocationRepository {

    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(context) }
    private val geocoder by lazy { Geocoder(context) }

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Flow<Location?> =
        callbackFlow {
            fusedLocationProviderClient
                .lastLocation
                .addOnSuccessListener { location ->
                    trySend(
                        location?.let {
                            Location(
                                coordinates = Pair(it.latitude, it.longitude),
                                address = null
                            )
                        }
                    )
                }
                .addOnCanceledListener { trySend(null) }
                .addOnFailureListener { trySend(null) }

            awaitClose()
        }

    @SuppressLint("MissingPermission")
    override fun getMyLocationCoordinates(): Flow<Location?> =
        callbackFlow {
            fusedLocationProviderClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    trySend(
                        location?.let {
                            Location(
                                coordinates = Pair(it.latitude, it.longitude),
                                address = null
                            )
                        }
                    )
                }
                .addOnCanceledListener { trySend(null) }
                .addOnFailureListener { trySend(null) }

            awaitClose()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMyLocationFullAddress(): Flow<Location?> =
        getMyLocationCoordinates()
            .flatMapLatest {
                it?.let { getFromCoordinates(it.coordinates) } ?: flowOf(null)
            }

    @Suppress("DEPRECATION")
    override fun getFromCoordinates(coordinates: Coordinates): Flow<Location?> =
        callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(coordinates.first, coordinates.second, 1) {
                    trySend(it.firstOrNull()?.toLocation())
                }
            } else {
                val address = geocoder.getFromLocation(coordinates.first, coordinates.second, 1)
                    ?.firstOrNull()

                trySend(address?.toLocation())
            }

            awaitClose()
        }

    @Suppress("DEPRECATION")
    override fun getFromName(name: String): Flow<Location?> =
        callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(name, 1) {
                    trySend(it.firstOrNull()?.toLocation())
                }
            } else {
                val address = geocoder.getFromLocationName(name, 1)
                    ?.firstOrNull()

                trySend(address?.toLocation())
            }

            awaitClose()
        }
}