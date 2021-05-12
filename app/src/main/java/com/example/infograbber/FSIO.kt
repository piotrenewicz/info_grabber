@file:JvmName("fsio")

package com.example.infograbber
//import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*

const val defaultFileName:String = "infoStorage.json"

fun fsinit(): String{
    println("Storage not initialized!\nReturning defaults")
    return "[]"

}

fun fswrite(c: Context, data: String, fileName:String = defaultFileName){
    val fileOutputStream:FileOutputStream
    try{
        fileOutputStream = c.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream.write(data.toByteArray())
        fileOutputStream.close()
    } catch (e: FileNotFoundException){
        e.printStackTrace()
    }catch (e: NumberFormatException){
        e.printStackTrace()
    }catch (e: IOException){
        e.printStackTrace()
    }catch (e: Exception){
        e.printStackTrace()
    }
}

fun fsread(c: Context, fileName: String = defaultFileName): String{
    val data: String
    val fileInputStream:FileInputStream
    try{
        fileInputStream = c.openFileInput(fileName)
        data = fileInputStream.bufferedReader().readText()
        fileInputStream.close()
        return data
    } catch (e: FileNotFoundException){
        e.printStackTrace()
    }catch (e: NumberFormatException){
        e.printStackTrace()
    }catch (e: IOException){
        e.printStackTrace()
    }catch (e: Exception){
        e.printStackTrace()
    }

    return fsinit()
}


fun websiteListToJsonString(sites: List<Website>): String {
    return Json.encodeToString(sites)
}

fun jsonStringToWebsiteList(jstr:String): MutableList<Website> {
    return Json.decodeFromString(jstr)
}

// USE THIS THING TO LOAD YOUR LIST OF WEBSITES my brothers.
fun readWebsiteList(c:Context, fileName: String=defaultFileName):MutableList<Website>{
    return jsonStringToWebsiteList(fsread(c,fileName))
}

fun writeWebsite(c:Context, site:Website, fileName: String=defaultFileName){
    val list:MutableList<Website> = readWebsiteList(c)
    println("GOT DATA $list")
    list.add(site)

    val dataString = websiteListToJsonString(list)
    fswrite(c,dataString,fileName)
    println("DATAAAA $dataString")

}