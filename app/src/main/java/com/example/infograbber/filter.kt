@file:JvmName("filter")

package com.example.infograbber
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Runtime

fun syscall(commmand: String): String {
    var output: String
    try{
        val script: Array<String> = arrayOf("/system/bin/sh", "-c", commmand)
        val process: Process = Runtime.getRuntime().exec(script)
        val stdout = BufferedReader(InputStreamReader(process.inputStream))

        output = stdout.readText()

    }catch (e: IOException){
        e.printStackTrace();
        output = e.toString()
    }
    return output
}


//fun syscall_with_html(c: Context, html: String, command: String, callback: (output: String) -> Unit){
//    ramfs(c, "html_", html){ rampath ->
//        val rc: String = "alias html=\"cat $rampath\"\n$command\n"
//        callback(syscall(rc))
//    }
//}

fun syscall_smart(c: Context, command: String, url: String?, callback: (content: String) -> Unit){
    // are we even trying to download something?
    if(url.isNullOrEmpty()){
        // we aren't trying to download, just mask html call and run command then
        val rc: String = "alias html=\"echo 'Error: URL was not supplied!'\"\n$command\n"
        CoroutineScope(Dispatchers.IO).launch() {
            println("execution steering")
            callback(syscall(rc))
        }
        return
    }
    // ok so we have something in the url, but will it give html success??
    // let's try
    downloadhtml(c, url){ result ->
        if(result.isNullOrEmpty()){
            // download failed, prep info in html call and run anyway.
            val rc: String = "alias html=\"echo 'Error: fetching html failed!'\"\n$command\n"
            callback(syscall(rc))
            return@downloadhtml
        }
        // oh so we have some html let's goooo
        ramfs(c, "html_", result){ rampath ->
            val rc: String = "alias html=\"cat '$rampath'\"\n$command\n"
            callback(syscall(rc))
            return@ramfs
        }
        return@downloadhtml
    }
    return
}