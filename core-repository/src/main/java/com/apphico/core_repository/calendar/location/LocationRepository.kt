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

    fun getLastKnownLocation(context: Context): Flow<Location?>

    fun getMyLocationCoordinates(context: Context): Flow<Location?>

    fun getMyLocationFullAddress(context: Context): Flow<Location?>

    fun getFromCoordinates(context: Context, coordinates: Coordinates): Flow<Location?>

    fun getFromName(context: Context, name: String): Flow<Location?>
}

class LocationRepositoryImpl : LocationRepository {

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(context: Context): Flow<Location?> =
        callbackFlow {
            LocationServices.getFusedLocationProviderClient(context)
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
                }.addOnCanceledListener { trySend(null) }
                .addOnFailureListener { trySend(null) }

            awaitClose()
        }

    @SuppressLint("MissingPermission")
    override fun getMyLocationCoordinates(context: Context): Flow<Location?> =
        callbackFlow {
            LocationServices.getFusedLocationProviderClient(context)
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
    override fun getMyLocationFullAddress(context: Context): Flow<Location?> =
        getMyLocationCoordinates(context)
            .flatMapLatest {
                it?.let { getFromCoordinates(context, it.coordinates) } ?: flowOf(null)
            }

    @Suppress("DEPRECATION")
    override fun getFromCoordinates(
        context: Context,
        coordinates: Coordinates
    ): Flow<Location?> =
        callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Geocoder(context)
                    .getFromLocation(coordinates.first, coordinates.second, 1) {
                        trySend(it.firstOrNull()?.toLocation())
                    }
            } else {
                val address = Geocoder(context)
                    .getFromLocation(coordinates.first, coordinates.second, 1)
                    ?.firstOrNull()

                trySend(address?.toLocation())
            }

            awaitClose()
        }

    @Suppress("DEPRECATION")
    override fun getFromName(
        context: Context,
        name: String
    ): Flow<Location?> =
        callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Geocoder(context)
                    .getFromLocationName(name, 1) {
                        trySend(it.firstOrNull()?.toLocation())
                    }
            } else {
                val address = Geocoder(context)
                    .getFromLocationName(name, 1)
                    ?.firstOrNull()

                trySend(address?.toLocation())
            }

            awaitClose()
        }
}