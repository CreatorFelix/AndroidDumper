package com.creator.androiddumper.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.creator.androiddumper.R
import com.creator.androiddumper.dialog.AdjustTextDialog
import com.creator.androiddumper.extension.*
import com.creator.androiddumper.util.Constant
import com.creator.androiddumper.util.MemInfoAccessible
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_package_memifo.*

class PackageMemInfoActivity : RxAppCompatActivity(), MemInfoAccessible {

    private lateinit var mPackageName: String
    private var mHScrollView: HorizontalScrollView? = null
    private var mTvResult: TextView? = null
    private var autoNewlineEnable: Boolean = false
        set(enable) {
            if (field == enable) return
            field = enable
            scrollView.removeAllViews()
            mHScrollView?.removeAllViews()
            if (enable) {
                scrollView.addView(mTvResult)
            } else {
                scrollView.addView(mHScrollView)
                mHScrollView?.addView(mTvResult)
            }
            saveAutoNewlineState(enable)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPackageName = intent.getStringExtra(Constant.EXTRA_DETAIL_PACKAGE_NAME)
        if (packageName.isEmpty()) throw IllegalArgumentException("Current package name cannot be null!")
        setContentView(R.layout.activity_package_memifo)
        val typeface = Typeface.createFromAsset(assets, "consola.ttf")
        tvResult.typeface = typeface
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mPackageName
        mHScrollView = hScrollView
        mTvResult = tvResult
    }

    override fun onStart() {
        super.onStart()
        val preferences = getAppDefaultPreferences()
        tvResult.textSize = preferences.getFloat(Constant.KEY_CURRENT_SHELL_TEXT_SIZE, Constant.DEFAULT_SHELL_TEXT_SIZE)
        autoNewlineEnable = localAutoNewlineState()
        refreshMemInfo()
    }

    private fun refreshMemInfo() {
        progressBar.visibility = View.VISIBLE
        queryMemInfo(this,mPackageName, Consumer {
            progressBar.visibility = View.GONE
            tvResult.text = it
        })
    }

    private fun saveMemInfo() {
        if (TextUtils.isEmpty(tvResult.text)) return
        outputMemInfo(this,tvResult.text.toString(), mPackageName,
                Consumer { showToast(it, Toast.LENGTH_LONG) })
    }

    private fun showAdjustTextDialog() {
        AdjustTextDialog.Builder(this).create()
                .show(px2sp(tvResult.textSize) - Constant.MIN_SHELL_TEXT_SIZE,
                        object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, changed: Boolean) {
                                val newTextSize = progress.toFloat() + Constant.MIN_SHELL_TEXT_SIZE
                                tvResult.textSize = newTextSize
                                val editor = editDefaultPreferences()
                                editor.putFloat(Constant.KEY_CURRENT_SHELL_TEXT_SIZE, newTextSize)
                                editor.apply()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_package_meminfo, menu)
        menu?.findItem(R.id.item_auto_newline)?.isChecked = localAutoNewlineState()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_refresh -> refreshMemInfo()
            R.id.item_save -> saveMemInfo()
            R.id.item_adjust_text_size -> showAdjustTextDialog()
            R.id.item_auto_newline -> {
                item.isChecked = !item.isChecked
                autoNewlineEnable = item.isChecked
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}