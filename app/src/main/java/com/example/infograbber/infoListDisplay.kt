package com.example.infograbber

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.info_display_item.view.*

class infoListDisplay() : RecyclerView.Adapter<infoListDisplay.infoViewHolder>() {
    class infoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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