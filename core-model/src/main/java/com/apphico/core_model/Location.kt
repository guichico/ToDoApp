package com.apphico.core_model

import android.location.Address
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

typealias Coordinates = Pair<Double, Double>

@Stable
@Immutable
@Parcelize
@Serializable
data class Location(
    val id: Long = 0,
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
