package com.example.infograbber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var WebsiteAdapter: WebsiteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WebsiteAdapter = WebsiteAdapter(mutableListOf())
        AppWebsiteList.adapter = WebsiteAdapter
        AppWebsiteList.layoutManager = LinearLayoutManager(this)

        AppWebsiteSeconds.addTextChangedListener {
            if (it.toString().length == 1 && it.toString().startsWith("0")) {
                it!!.clear()
            }
        }

        AppDeleteButton.setOnClickListener {
            WebsiteAdapter.deleteWebsite()
        }

        AppAddButton.setOnClickListener {
            val websiteURL: String = AppWebsiteURL.text.toString()
            val websiteTimeInterval: String = AppWebsiteSeconds.text.toString()
            val websiteTitle: String = AppWebsiteTitle.text.toString()

            val website: Website = Website(this@MainActivity, websiteTitle, websiteURL, websiteTimeInterval.toInt())
            WebsiteAdapter.addWebsite(website)

            //Clearing out input fields
            AppWebsiteSeconds.text.clear()
            AppWebsiteURL.text.clear()
            AppWebsiteTitle.text.clear()
        }
    }
}
