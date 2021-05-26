package com.example.infograbber
import kotlinx.serialization.Serializable



@Serializable
data class Website(val title: String,
                   val URL:String,
                   val timeInterval: Int,
                   val awlCommand: String = "",
                   val domain: String = "https://www.example.com",
                   var isChecked: Boolean = false)