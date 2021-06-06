package com.example.infograbber
import kotlinx.serialization.Serializable



@Serializable
data class infoElement(val title: String,
                   val URL:String,
                   val awlCommand: String = "",
                   val domain: String = "https://www.example.com")