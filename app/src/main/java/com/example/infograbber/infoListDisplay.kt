package com.example.infograbber

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.info_display_item.view.*


class infoListDisplay(var c:Context) : RecyclerView.Adapter<infoListDisplay.infoViewHolder>() {
    inner class infoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener, View.OnLongClickListener {
        init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

        override fun onClick(view: View) {

        }

        override fun onLongClick(view: View): Boolean {
            println("HERE ${this.layoutPosition}")

            val intent = Intent(view.context, InfoItemEdit::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            val b = Bundle()
            b.putInt("key", 1) //Your id

            intent.putExtras(b) //Put your id to your next Intent
            c.startActivity(intent)
            return true
        }
    }
    private var infoList: MutableList<infoElement> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): infoViewHolder {
        return infoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.info_display_item, parent, false))
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    fun loadInfoList(c: Context){
        infoList = readInfoList(c)

        notifyDataSetChanged()
    }

    fun refreshAllInfo(a: Activity){
        val c: Context = a
        infoList.forEachIndexed{ index, infoEl ->
            downloadhtml(c, infoEl.URL){ result ->
                if (result != null) {
                    syscall_with_html(c, result, infoEl.awlCommand){ output ->
                        a.runOnUiThread {
                            notifyItemChanged(index, output)
                        }
                    }
                }else{
                    val output: String = syscall(infoEl.awlCommand)
                    a.runOnUiThread{
                        notifyItemChanged(index, output)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: infoViewHolder, position: Int, payloads: List<Any>) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            holder.itemView.apply{
               InfoContent_field.text = payloads[0] as String
            }
        }
    }

    override fun onBindViewHolder(holder: infoViewHolder, position: Int) {
        val curInfoEl = infoList[position]
        holder.itemView.apply {
            InfoTitle_field.text = curInfoEl.title
            WebsiteDomain.text = curInfoEl.domain
        }
    }
}