package com.example.myapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cats(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable