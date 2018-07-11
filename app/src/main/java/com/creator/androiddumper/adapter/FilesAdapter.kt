package com.creator.androiddumper.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.FileProvider
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.creator.androiddumper.BuildConfig
import com.creator.androiddumper.R
import com.creator.androiddumper.extension.toFormattedFileSize
import com.creator.androiddumper.extension.toFormattedTime
import com.creator.androiddumper.util.InfoFile
import com.creator.androiddumper.util.StickyItemHeaderAdapter
import java.io.File
import java.net.URLConnection

class FilesAdapter(private val context: Context, private val enableItemHeader: Boolean = true,
                   files: Array<InfoFile>? = null) :
        StickyItemHeaderAdapter<FilesAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    var mFiles: Array<InfoFile>? = files
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_saved_file, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFile = mFiles!![position]
        holder.tvPkgName.visibility = if (enableItemHeader && willShowItemHeader(position)) {
            holder.tvPkgName.text = getHeaderText(position)
            View.VISIBLE
        } else View.GONE
        holder.tvLength.text = currentFile.length.toFormattedFileSize()
        holder.tvLastModified.text = currentFile.lastModified.toFormattedTime(context.resources)
        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(FileProvider.getUriForFile(context,
                    "${BuildConfig.APPLICATION_ID}.fileProvider",
                    File(currentFile.absolutePath)),
                    URLConnection.getFileNameMap().getContentTypeFor(currentFile.name))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mFiles?.size ?: 0
    }

    override fun getHeaderText(position: Int): String {
        return mFiles?.get(position)?.packageName ?: ""
    }

    override fun willShowItemHeader(position: Int): Boolean {
        if (position == 0 || position > itemCount - 1) return true
        val preFile = mFiles?.get(position - 1)
        val currentFile = mFiles?.get(position)
        return preFile?.packageName != currentFile?.packageName ?: false
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLastModified: TextView = itemView.findViewById(R.id.tv_last_modified_time)
        val tvPkgName: TextView = itemView.findViewById(R.id.tv_package_name)
        val tvLength: TextView = itemView.findViewById(R.id.tv_length)
    }
}