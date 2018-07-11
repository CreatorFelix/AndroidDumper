package com.creator.androiddumper.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.creator.androiddumper.R
import com.creator.androiddumper.adapter.FilesAdapter
import com.creator.androiddumper.extension.setStickyHeader
import com.creator.androiddumper.util.Constant
import com.creator.androiddumper.util.MemInfoAccessible
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_saved_files.*

class SavedFilesActivity : RxAppCompatActivity(), MemInfoAccessible {

    private var mPackageName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_files)
        mPackageName = intent.getStringExtra(Constant.EXTRA_DETAIL_PACKAGE_NAME)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.label_saved_files)
        supportActionBar?.subtitle = mPackageName
        rvSavedFiles.layoutManager = LinearLayoutManager(this)
        rvSavedFiles.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rvSavedFiles.adapter = FilesAdapter(this, mPackageName == null)
        if (mPackageName != null)
            tvTopPkgName.visibility = View.GONE
        else
            rvSavedFiles.setStickyHeader(tvTopPkgName as TextView)
    }

    override fun onStart() {
        super.onStart()
        val adapter = rvSavedFiles.adapter as FilesAdapter
        adapter.mFiles = loadSavedFiles(mPackageName)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}