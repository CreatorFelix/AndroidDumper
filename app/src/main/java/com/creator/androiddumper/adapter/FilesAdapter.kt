package com.creator.androiddumper.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.creator.androiddumper.R
import java.io.File

class FilesAdapter(context: Context, files: Array<File>? = null) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    var mFiles: Array<File>? = files
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_saved_file, parent, false))
    }

    override fun getItemCount(): Int {
        return mFiles?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFile = mFiles?.get(position)
        holder.tvFileItem.text = currentFile?.name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFileItem: TextView = itemView.findViewById(R.id.tvFileItem)
    }

}