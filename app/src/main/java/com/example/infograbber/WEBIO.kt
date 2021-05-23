@file:JvmName("webio")

package com.example.infograbber
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
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

