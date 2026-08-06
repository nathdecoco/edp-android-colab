package com.example.myapplication

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
object Home

@Serializable
data class Greeting(val userName: String)
