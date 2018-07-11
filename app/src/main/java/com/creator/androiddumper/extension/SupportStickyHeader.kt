package com.creator.androiddumper.extension

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.creator.androiddumper.util.StickyItemHeaderAdapter

/**
 * @author Felix.Liang
 */
fun RecyclerView.setStickyHeader(headerView: TextView) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (layoutManager !is LinearLayoutManager || adapter !is StickyItemHeaderAdapter) return
            val llm = layoutManager as LinearLayoutManager
            val pos = llm.findFirstVisibleItemPosition()
            val firstVisible = llm.findViewByPosition(pos) ?: return
            val stickyAdapter = adapter as StickyItemHeaderAdapter
            headerView.text = stickyAdapter.getHeaderText(pos)
            if (stickyAdapter.willShowItemHeader(pos + 1)
                    && firstVisible.bottom <= headerView.height) {
                headerView.translationY = firstVisible.bottom.toFloat() - headerView.height
            } else {
                headerView.translationY = 0F
            }
        }
    })
}