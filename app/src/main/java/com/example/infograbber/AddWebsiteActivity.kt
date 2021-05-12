package com.example.infograbber

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_website.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class AddWebsiteActivity : AppCompatActivity() {
    //private lateinit var WebsiteAdapter: WebsiteAdapter

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
        setContentView(R.layout.activity_add_website)

        //WebsiteAdapter = WebsiteAdapter(mutableListOf())
        //AppWebsiteList.adapter = WebsiteAdapter
        //AppWebsiteList.layoutManager = LinearLayoutManager(this)

        //Preventing against user trying input number starting with 0 in website refresh time interval field
        AddWebsiteSeconds.addTextChangedListener {
            if (it.toString().length == 1 && it.toString().startsWith("0")) {
                it!!.clear()
            }
        }

        AddWebsiteReturn.setOnClickListener{
            finish()
        }

        AddWebsiteButtonAdd.setOnClickListener {
            val websiteURL: String = AddWebsiteURL.text.toString()
            var websiteTitle: String = AddWebsiteTitle.text.toString()
            var websiteTimeInterval: Int = 0
            val websiteDomain: String = findWebsiteDomain(websiteURL)
            val websiteCommand: String = AddWebsiteCommand.text.toString()

            if(AddWebsiteSeconds.text.isEmpty()){
                Toast.makeText(this, "Wrong refresh interval", Toast.LENGTH_LONG).show()
            }else{
                websiteTimeInterval = AddWebsiteSeconds.text.toString().toInt()
            }

            CoroutineScope(Dispatchers.IO).launch(){
                val websiteSource: Document? = getWebsiteSource(this@AddWebsiteActivity, websiteURL)
                var result: String= ""
                if (websiteSource == null){
                    result = "Error while fetching website"
                }else{
                    websiteTitle = if(websiteTitle.isEmpty()) websiteSource.title() else websiteTitle
                    result = websiteSource.html()

                    if(websiteTimeInterval > 0){
                        // HERE TO FIX: website source is too long to be serialized, must be removed from class constructor
                        val website: Website = Website(websiteTitle, websiteURL, websiteTimeInterval, websiteCommand, "websiteSource.toString()", websiteDomain)
                        //Here goes code for inserting website into JSON database
                        writeWebsite(applicationContext,website)
                    }
                }
                println("Website: $result")
            }

            //Clearing out input fields
            AddWebsiteSeconds.text.clear()
            AddWebsiteURL.text.clear()
            AddWebsiteTitle.text.clear()
            AddWebsiteCommand.text.clear()
        }
    }
}