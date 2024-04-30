package com.example.btp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TripParameters(
    val destination: Location,
    val checkInDate: String,
    val checkOutDate: String,
    val originalBudget: Double,
    var remainingBudget: Double,
    val guests: Int
) : Parcelable