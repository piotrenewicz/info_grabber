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

        //Website item example
        val websiteTitle: String = "Onet.pl"
        val websiteURL: String = "https://www.onet.pl"
        val websiteTimeInterval: Int = 30

        val website: Website = Website(websiteTitle, websiteURL, websiteTimeInterval)
        //Adding example website to recyclerView Adapter
        WebsiteAdapter.addWebsite(website)

        // START try json read
        // No sites no data – add one via application and restart it, it works
        // How to refresh this list when we enter into this activity, not only on creation?
        val websites = readWebsiteList(applicationContext)
        for (site in websites)
        {
            println("WEBSITE $site")
            WebsiteAdapter.addWebsite(site)
        }
        // END try json read

    }
}
