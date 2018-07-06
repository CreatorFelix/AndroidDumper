package com.creator.androiddumper.util

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import java.util.regex.Pattern

/**
 * @author Felix.Liang
 */
class TextLinker private constructor() {

    companion object {

        private const val REGEX_PACKAGE_WRAPPER = "[:]\\s+[a-zA-Z.][a-zA-Z0-9.:@_-]*[a-zA-Z0-9]\\s+[(]"
        private const val REGEX_PACKAGE = "[a-zA-Z.][a-zA-Z0-9.:@_-]*[a-zA-Z0-9]"
        private val sPatternPkgWrapper = Pattern.compile(REGEX_PACKAGE_WRAPPER)
        private val sPatternPkg = Pattern.compile(REGEX_PACKAGE)

        fun generateLinkedText(source: String?, listener: OnTextClickListener?): SpannableString {
            return when {
                TextUtils.isEmpty(source) -> SpannableString("")
                else -> {
                    val spanString = SpannableString(source)
                    val matcher = sPatternPkgWrapper.matcher(source)
                    while (matcher.find()) {
                        var found = matcher.group()
                        val m = sPatternPkg.matcher(found)
                        var startOffset = 0
                        var endOffset = 0
                        if (m.find()) {
                            startOffset = m.start()
                            val endIndex = m.end()
                            endOffset = endIndex - found.length
                            found = found.substring(startOffset, endIndex)
                        }
                        val start = matcher.start() + startOffset
                        val end = matcher.end() + endOffset
                        spanString.setSpan(PackageSpan(found, listener),
                                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    return spanString
                }
            }
        }
    }

    interface OnTextClickListener {
        fun onTextClick(text: String, widget: View)
    }

    class PackageSpan(private val text: String, private val listener: OnTextClickListener?) : ClickableSpan() {

        override fun onClick(widget: View) {
            listener?.onTextClick(text, widget)
        }

        override fun updateDrawState(ds: TextPaint?) {
            super.updateDrawState(ds)
            ds?.color = Color.parseColor("#2E76FC")
        }
    }
}