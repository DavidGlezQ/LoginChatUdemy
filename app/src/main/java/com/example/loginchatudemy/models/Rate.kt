package com.example.loginchatudemy.models

import java.util.*

data class Rate(
    val text: String,
    val rate: Float,
    val createAt: Date,
    val profileImgUrl: String = ""
)