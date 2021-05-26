package com.example.infograbber

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

//import android.widget.Toast
//import java.io.*

class fsrw_example : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fsrw_example)

        val fileName = findViewById<EditText>(R.id.editFile)
        val fileData = findViewById<EditText>(R.id.editData)

        val btnView = findViewById<Button>(R.id.btnView)


        btnView.setOnClickListener(View.OnClickListener {
            val filename = fileName.text.toString()
//            if(filename.toString()!=null && filename.toString().trim()!=""){
//                var fileInputStream: FileInputStream? = null
//                fileInputStream = openFileInput(filename)
//                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
//                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
//                val stringBuilder: StringBuilder = StringBuilder()
//                var text: String? = null
//                while ({ text = bufferedReader.readLine(); text }() != null) {
//                    stringBuilder.append(text)
//                }
//                Displaying data on EditText
//                fileData.setText(stringBuilder.toString()).toString()
//            }else{
//                Toast.makeText(applicationContext,"file name cannot be blank",Toast.LENGTH_LONG).show()
//            }
            downloadhtml(this, filename){ result ->
                runOnUiThread {
                    // Stuff that updates the UI
                    fileData.setText(result)
                }
            }
        })

    }

}