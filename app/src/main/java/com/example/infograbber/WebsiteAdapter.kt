package com.example.infograbber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_website.view.*

class WebsiteAdapter(val websites: MutableList<Website>) : RecyclerView.Adapter<WebsiteAdapter.WebsiteViewHolder>() {
    class WebsiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteViewHolder {
        return WebsiteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_website, parent, false))
    }

    override fun getItemCount(): Int {
        return websites.size
    }

    fun addWebsite(website: Website){
        websites.add(website)
        notifyItemInserted(websites.size - 1)
    }

    fun deleteWebsite(){
        websites.removeAll { website ->
            website.isChecked
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WebsiteViewHolder, position: Int) {
        val curWebsite = websites[position]
        holder.itemView.apply{
          WebsiteTitle.text = curWebsite.title
          WebsiteIsChecked.isChecked = curWebsite.isChecked
            WebsiteDomain.text = curWebsite.domain
          WebsiteIsChecked.setOnCheckedChangeListener { _, isChecked ->
            curWebsite.isChecked = !curWebsite.isChecked
          }
        }

    }
}