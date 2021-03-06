package com.example.infograbber

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_info_source.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddInfoSourceActivity : AppCompatActivity() {
    var info_item_index = -1
    //private lateinit var WebsiteAdapter: WebsiteAdapter

//    private suspend fun getWebsiteSource(context: Context, URL: String): Document? = try {
//        withContext(Dispatchers.Main){
//            AddWebsiteHTMLCode.text = "Test Test Test\nTest\nTest\nTest\nTest"
//        }
//        val doc: Document = Jsoup.connect(URL).get()
//        println(doc.title())
//        withContext(Dispatchers.Main){
//            AddWebsiteHTMLCode.text = doc.html()
//        }
//        doc
//    }catch(e: Exception){
//        withContext(Dispatchers.Main){
//            Toast.makeText(context, "Error fetching URL", Toast.LENGTH_LONG).show()
//        }
//        null
//    }

    private fun findWebsiteDomain(url: String): String = try{
        Regex("^(?:https?:\\/\\/)?(?:[^@\n]+@)?(?:www\\.)?([^:\\/\n?]+)").find(url)!!.groupValues[1]
    }catch(e: NullPointerException){
        "Unknown domain"
    }

    private fun fillForIndex(index:Int){
        if (index!=-1){
            val infoItem = getInfoEl(applicationContext, index)
            InfoTitle_field.setText(infoItem.title)
            WebsiteURL_field.setText(infoItem.URL)
            Command_field.setText(infoItem.awlCommand)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_info_source)

        if (intent.extras != null) info_item_index = intent.extras!!.getInt("info_item_index") else -1
        println("GOT KEY: $info_item_index")

        fillForIndex(info_item_index)

        //WebsiteAdapter = WebsiteAdapter(mutableListOf())
        //AppWebsiteList.adapter = WebsiteAdapter
        //AppWebsiteList.layoutManager = LinearLayoutManager(this)

        //Preventing against user trying input number starting with 0 in website refresh time interval field
        ActivityReturn_button.setOnClickListener{
            finish()
        }


        Save_button.setOnClickListener {
            val websiteURL: String = WebsiteURL_field.text.toString()
            var infoTitle: String = InfoTitle_field.text.toString()
            val websiteDomain: String = findWebsiteDomain(websiteURL)
            val command: String = Command_field.text.toString()

            if (infoTitle.isEmpty() and websiteURL.isNotEmpty()){
                runBlocking {
                    CoroutineScope(Dispatchers.IO).launch {              /// here problem Konrad, Do Coroutine Scope Dispatcher IO, and wait.
                        println("tring to gen")
                        val WebTitle: String? = getWebsiteTitle(applicationContext, websiteURL)
                        if (!WebTitle.isNullOrEmpty()){
                            infoTitle = WebTitle
                        }
                    }.join()
                }
            }
            println("see ya suckez")
            val infoEl = infoElement(infoTitle, websiteURL, command,  websiteDomain)
            if (info_item_index!= -1){
                updateInfoElement(applicationContext,info_item_index, infoEl)
            }else{
                appendInfoElement(applicationContext, infoEl)
            }
            //Clearing out input fields
            WebsiteURL_field.text.clear()
            InfoTitle_field.text.clear()
            Command_field.text.clear()
            finish()
        }

        Fetch_button.setOnClickListener{
            val websiteURL: String = WebsiteURL_field.text.toString()
            downloadhtml(this, websiteURL){ result ->
                runOnUiThread {
                    if(result.isNullOrEmpty()){
                        Toast.makeText(this, "Error fetching URL", Toast.LENGTH_LONG).show()
                        BigResult_field.text = "Err"
                    }else{
                        BigResult_field.text = result
                    }
                }
            }

        }

        ExecuteCommand_button.setOnClickListener{
            val url: String = WebsiteURL_field.text.toString()
            val command: String = Command_field.text.toString()
//            println("$url, $command")
            syscall_smart(this, command, url){ output ->
                runOnUiThread {
                    BigResult_field.text = output
                }
            }
        }

        if (info_item_index == -1){
            DeleteInfo_button.visibility = View.INVISIBLE
        }else{
            DeleteInfo_button.visibility = View.VISIBLE
        }
        DeleteInfo_button.setOnClickListener{
            updateInfoList(this){ infoList ->
                infoList.removeAt(info_item_index)
            }
            finish()
        }

        //TextTest.text =  tu jaki?? tekst dla pola tekstowego

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