package com.creator.androiddumper.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.widget.SeekBar
import com.creator.androiddumper.R
import com.creator.androiddumper.util.Constant
import kotlinx.android.synthetic.main.dialog_adjust_text.*

/**
 * @author Felix.Liang
 */
class AdjustTextDialog(context: Context) : AlertDialog(context) {

    fun show(currentTextSize: Float, callback: SeekBar.OnSeekBarChangeListener) {
        super.show()
        window.setDimAmount(0.1F)
        window.setBackgroundDrawable(null)
        window.setGravity(Gravity.TOP)
        window.setContentView(R.layout.dialog_adjust_text)
        seekBar.progress = currentTextSize.toInt()
        seekBar.max = Constant.MAX_SHELL_TEXT_SIZE
        seekBar.setOnSeekBarChangeListener(callback)
    }

    class Builder(context: Context) : AlertDialog.Builder(context) {

        override fun create(): AdjustTextDialog {
            return AdjustTextDialog(context)
        }
    }
}