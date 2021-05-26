@file:JvmName("webio")

package com.example.infograbber
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception


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

fun downloadhtml(c: Context, URL: String, callback) {
    CoroutineScope(Dispatchers.IO).launch() {
        val websiteSource: Document? = getWebsiteSource(c, URL)
        var result: String = ""
        if (websiteSource == null) {
            result = "Error while fetching website"
        } else {
            result = websiteSource.html()
        }
        println("Website: $result")
        callback(result)
    }
}