@file:JvmName("fsio")

package com.example.infograbber
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
    val fileOutputStream: FileOutputStream
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
    val fileInputStream: FileInputStream
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


fun ramfs(c: Context, fileName: String, content: String, callback: (rampath: String) -> Unit){
    val file: File = File.createTempFile(fileName, null, c.cacheDir)
    file.writeText(content)
    callback(c.cacheDir.toString() + "/" + file.name)
    file.delete()
}

fun readInfoList(c:Context, fileName: String=defaultFileName):MutableList<infoElement>{
    return Json.decodeFromString(fsread(c,fileName))
}

fun updateInfoList(c: Context, fileName: String = defaultFileName, callback: (infoList: MutableList<infoElement>) -> Unit){
    val infoList:MutableList<infoElement> = readInfoList(c, fileName)
    callback(infoList)
    fswrite(c, Json.encodeToString(infoList), fileName)
}

fun appendInfoElement(c:Context, el:infoElement, fileName: String=defaultFileName) {
    updateInfoList(c, fileName) { infoList ->
        infoList.add(el)
    }
}

fun getInfoEl(c: Context, index: Int, fileName: String = defaultFileName): infoElement {
    return readInfoList(c, fileName)[index]
}

//
//
//
//
//fun infoListToJsonString(sites: List<infoElement>): String {
//    return Json.encodeToString(sites)
//}
//
//fun jsonStringToInfoList(jstr:String): MutableList<infoElement> {
//    return Json.decodeFromString(jstr)
//}
//
//// USE THIS THING TO LOAD YOUR LIST OF WEBSITES my brothers.
//
//fun writeInfoList(c: Context, infoList: MutableList<infoElement>, fileName: String= defaultFileName){
//   fswrite(c, infoListToJsonString(infoList), fileName)
//}
//
//
////    val list:MutableList<infoElement> = readInfoList(c, fileName)
//////    println("GOT DATA $list")
////    list.add(el)
////    writeInfoList(c, list, fileName)
//
//
////fun getInfoElement(c:Context, idx: Int)
//// is the id in WebsiteAdapter predicatable?
//// there should be funs allowing to load infoEl, for edit.
//// update infoEl, after edit
//// and remove infoEl.