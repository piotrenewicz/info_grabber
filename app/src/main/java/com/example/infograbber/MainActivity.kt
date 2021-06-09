package com.example.infograbber
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var infoListDisplay: infoListDisplay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        infoListDisplay = infoListDisplay(applicationContext)
        AppWebsiteList.adapter = infoListDisplay
        AppWebsiteList.layoutManager = LinearLayoutManager(this)

        AppAddButton.setOnClickListener{
            val intent = Intent(this, AddInfoSourceActivity::class.java)
            startActivity(intent)
        }

        Refresh_button.setOnClickListener{
            infoListDisplay.refreshAllInfo(this)
        }
    }

    override fun onStart() {
        super.onStart()
        infoListDisplay.loadInfoList(this)
        infoListDisplay.refreshAllInfo(this)
    }


}
