package com.creator.androiddumper.util

import android.support.v7.widget.RecyclerView

/**
 * @author Felix.Liang
 */
abstract class StickyItemHeaderAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun willShowItemHeader(position: Int): Boolean

    abstract fun getHeaderText(position: Int): String
}