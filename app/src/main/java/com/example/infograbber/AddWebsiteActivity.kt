package com.example.infograbber

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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
        withContext(Dispatchers.Main){
            AddWebsiteHTMLCode.text = "Test Test Test\nTest\nTest\nTest\nTest"
        }
        val doc: Document = Jsoup.connect(URL).get()
        println(doc.title())
        withContext(Dispatchers.Main){
            AddWebsiteHTMLCode.text = doc.html()
        }
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

            val website: Website = Website(websiteTitle, websiteURL, websiteTimeInterval, websiteCommand,  websiteDomain)
            writeWebsite(applicationContext,website)
            //Clearing out input fields
            AddWebsiteSeconds.text.clear()
            AddWebsiteURL.text.clear()
            AddWebsiteTitle.text.clear()
            AddWebsiteCommand.text.clear()
        }

        AddWebsiteTest.setOnClickListener{
            val websiteURL: String = AddWebsiteURL.text.toString()
            downloadhtml(this, websiteURL){ result ->
                runOnUiThread {
                    AddWebsiteHTMLCode.text = result
                }
            }

        }
        
        AddWebsiteTest2.setOnClickListener{ //TO DO dla przycisk 2
//            println("button alive")
//            filter_testing()
//            println("button job done")
            html_inserter(this, AddWebsiteHTMLCode.text.toString(), AddWebsiteCommand.text.toString()){ output ->
                TextTest.text = output
            }
        }

        AddWebsiteTest3.setOnClickListener{
            //TO DO dla przycisk 3
        }

        //TextTest.text =  tu jakiś tekst dla pola tekstowego

        //   use like this:
//
//            downloadhtml(this, url){ result ->
//                runOnUiThread {
//                    // Stuff that updates the UI
//                    fileData.setText(result)
//                }
//            }

    }
}