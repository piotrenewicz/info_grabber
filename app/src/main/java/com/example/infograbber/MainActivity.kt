package com.example.infograbber
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        AppAddButton.setOnClickListener{
            val intent = Intent(this, AddWebsiteActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        val websites = readWebsiteList(applicationContext)
        for (site in websites)
        {
            WebsiteAdapter.addWebsite(site)
        }
    }
}
