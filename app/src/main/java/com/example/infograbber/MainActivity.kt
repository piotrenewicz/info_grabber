package com.example.infograbber

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var WebsiteAdapter: WebsiteAdapter

    private suspend fun getWebsiteSource(context: Context, URL: String): Document? = try {
        val doc: Document = Jsoup.connect(URL).get()
        println(doc.title())
        doc
    }catch(e: Exception){
        withContext(Dispatchers.Main){
            Toast.makeText(context, "Error fetching URL", Toast.LENGTH_LONG).show()
        }
        null
    }

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

            CoroutineScope(IO).launch(){
                val websiteSource: Document? = getWebsiteSource(this@MainActivity, websiteURL)
                var result: String= ""
                if (websiteSource == null){
                    result = "Error while fetching website"
                }else{
                    result = websiteSource.html()
                }
                println("Website: $result")
            }
        }
    }
}
