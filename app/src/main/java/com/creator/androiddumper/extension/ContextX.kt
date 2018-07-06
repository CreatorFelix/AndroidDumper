package com.creator.androiddumper.extension

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.creator.androiddumper.R
import com.creator.androiddumper.activity.AllMemInfoActivity
import com.creator.androiddumper.activity.PackageMemInfoActivity
import com.creator.androiddumper.activity.SavedFilesActivity
import com.creator.androiddumper.util.Constant
import io.reactivex.annotations.NonNull

/**
 * @author Felix.Liang
 */
fun Context.navigateToPackageMemInfo(@NonNull detailPkgName: String) {
    val next = Intent(this, PackageMemInfoActivity::class.java)
    next.putExtra(Constant.EXTRA_DETAIL_PACKAGE_NAME, detailPkgName)
    next.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(next)
}

fun Context.navigateToSavedFiles(detailPkgName: String? = null) {
    val next = Intent(this, SavedFilesActivity::class.java)
    if (detailPkgName != null) next.putExtra(Constant.EXTRA_DETAIL_PACKAGE_NAME, detailPkgName)
    next.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(next)
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, duration).show()

fun Context.showNotification() {
    val nm = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(Constant.CHANNEL_ID,
                getString(R.string.channel_name), NotificationManager.IMPORTANCE_LOW)
        nm.createNotificationChannel(channel)
    }
    val intent = Intent(this, AllMemInfoActivity::class.java)
    val taskStackBuilder = TaskStackBuilder.create(this)
            .addParentStack(AllMemInfoActivity::class.java)
            .addNextIntentWithParentStack(intent)
    val pendingIntent = taskStackBuilder.getPendingIntent(0,
            PendingIntent.FLAG_UPDATE_CURRENT)
    val notification = NotificationCompat.Builder(this, Constant.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentText(getString(R.string.notification_content_text))
            .setContentTitle(getString(R.string.notification_content_title))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    nm.notify(Constant.NOTIFICATION_ID, notification)
}

fun Context.getAppDefaultPreferences(): SharedPreferences {
    return getSharedPreferences(Constant.PREFERENCES_NAME, Context.MODE_PRIVATE)
}

fun Context.editDefaultPreferences(): SharedPreferences.Editor {
    return getAppDefaultPreferences().edit()
}

fun Context.px2sp(px: Float): Float {
    val fontScale = resources.displayMetrics.scaledDensity
    return px / fontScale + 0.5f
}

fun AppCompatActivity.localAutoNewlineState(): Boolean {
    return getAppDefaultPreferences().getBoolean(Constant.KEY_CURRENT_AUTO_NEWLINE, false)
}

fun AppCompatActivity.saveAutoNewlineState(enable: Boolean) {
    val editor = editDefaultPreferences()
    editor.putBoolean(Constant.KEY_CURRENT_AUTO_NEWLINE, enable)
    editor.apply()
}

