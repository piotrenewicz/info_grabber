@file:JvmName("filter")

package com.example.infograbber
import android.content.Context
import kotlinx.android.synthetic.main.activity_add_website.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Runtime

fun syscall(commmand: String): String {
    var out: String
    try{
        val script: Array<String> = arrayOf("/system/bin/sh", "-c", commmand)
        val process: Process = Runtime.getRuntime().exec(script)
        val std: BufferedReader = BufferedReader(InputStreamReader(process.inputStream))
        process.waitFor()
        out = std.readText()
        println(""" 
            !Command run!""".trimIndent())
        println(commmand)
        println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        println(out)
        println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n")
    }catch (e: IOException){
        e.printStackTrace();
        out = e.toString()
    }
    return out
}


fun html_inserter(c: Context, html: String, command: String, callback: (output: String) -> Unit){
    ramfs(c, "html_", html){ rampath ->
        val rc: String = "alias html=\"cat $rampath\"\n$command\n"
        callback(syscall(rc))
    }
//    val rc: String = """
//alias html='echo \'""" + html + """\''
//""" + command

}

fun filter_testing(){
    println("pretest")
    println(syscall("echo lol"))
    println("posttest")
}