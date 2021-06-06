@file:JvmName("filter")

package com.example.infograbber
import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Runtime

fun syscall(commmand: String): String {
    var output: String
    try{
        val script: Array<String> = arrayOf("/system/bin/sh", "-c", commmand)
        val process: Process = Runtime.getRuntime().exec(script)
        val stdout: BufferedReader = BufferedReader(InputStreamReader(process.inputStream))
        process.waitFor()
        output = stdout.readText()
//        println("""
//            !Command run!""".trimIndent())
//        println(commmand)
//        println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
//        println(out)
//        println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n")
    }catch (e: IOException){
        e.printStackTrace();
        output = e.toString()
    }
    return output
}


fun syscall_with_html(c: Context, html: String, command: String, callback: (output: String) -> Unit){
    ramfs(c, "html_", html){ rampath ->
        val rc: String = "alias html=\"cat $rampath\"\n$command\n"
        callback(syscall(rc))
    }
}