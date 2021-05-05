package com.example.infograbber

import android.content.Context

class Website(context: Context,
              val title: String,
              val URL:String,
              val timeInterval: Int,
              val domain: String = "example.com",
              var isChecked: Boolean = false) {}