package com.apphico.core_model

import android.location.Address
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

typealias Coordinates = Pair<Double, Double>

@Parcelize
data class Location(
    val coordinates: Coordinates,
    val address: String? = null
) : Parcelable

fun Address.toLocation() =
    Location(
        coordinates = Pair(this.latitude, this.longitude),
        address = this.getAddressLine(0)
    )

fun Coordinates.toLatLng() = LatLng(this.first, this.second)

fun LatLng.toCoordinates() = Pair(this.latitude, this.longitude)

fun Coordinates.toLocation() = Location(
    coordinates = Pair(this.first, this.second),
    address = null
)
