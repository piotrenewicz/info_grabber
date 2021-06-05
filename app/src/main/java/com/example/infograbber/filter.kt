@file:JvmName("filter")

package com.example.infograbber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Runtime

fun syscall(commmand: String): String {
    var out: String
    try{
        val process: Process = Runtime.getRuntime().exec(commmand)
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

fun filter_testing(){
    println("pretest")
    println(syscall("echo lol"))
    println("posttest")
}