package com.example.infograbber
import org.jsoup.nodes.Document

data class Website(val title: String, val URL:String, val timeInterval: Int, val awlCommand: String = "", val source: Document? = null,
              val domain: String = "example.com", var isChecked: Boolean = false)