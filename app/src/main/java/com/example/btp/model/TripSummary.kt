package com.example.btp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TripSummary(
    val id: Int? = 0,
    val tripParameters: TripParameters,
) : Parcelable
