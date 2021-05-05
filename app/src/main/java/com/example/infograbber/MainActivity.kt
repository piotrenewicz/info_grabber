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

    private fun findWebsiteDomain(url: String): String = try{
        Regex("^(?:https?:\\/\\/)?(?:[^@\n]+@)?(?:www\\.)?([^:\\/\n?]+)").find(url)!!.groupValues[1]
    }catch(e: java.lang.NullPointerException){
        "Unknown domain"
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
            val websiteTitle: String = AppWebsiteTitle.text.toString()
            val websiteTimeInterval: Int
            val websiteDomain: String = findWebsiteDomain(websiteURL)

            try{
                websiteTimeInterval = AppWebsiteSeconds.text.toString().toInt()
                val website = Website(this@MainActivity, websiteTitle, websiteURL, websiteTimeInterval, websiteDomain)
                WebsiteAdapter.addWebsite(website)
            }
            catch (e:NumberFormatException){
                println("HERE we go")
                Toast.makeText(applicationContext, "Wrong value for duration", Toast.LENGTH_LONG).show()
            }



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
