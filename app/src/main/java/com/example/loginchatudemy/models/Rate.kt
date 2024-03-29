package com.example.loginchatudemy.models

import java.util.*

data class Rate(
    val userId: String = "",
    val text: String = "",
    val rate: Float = 0f,
    val createAt: Date = Date(),
    val profileImgUrl: String = ""
)