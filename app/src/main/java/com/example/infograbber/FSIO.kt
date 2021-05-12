@file:JvmName("fsio")

package com.example.infograbber
//import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import java.io.*

const val defaultFileName:String = "infoStorage.json"

fun fsinit(): String{
    return "Storage not initialized!\nReturning defaults"

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