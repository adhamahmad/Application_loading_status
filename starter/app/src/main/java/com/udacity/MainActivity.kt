package com.udacity

import NotificationHelper
import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var downloadButton: LoadingButton

    private lateinit var fileName:String
    private lateinit var fileStatus:String
    private lateinit var manager:DownloadManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT,
            false,
            getString(R.string.app_name),
            getString(R.string.app_description)
        )

        downloadButton = findViewById<LoadingButton>(R.id.custom_button)

        val glideButton = findViewById<RadioButton>(R.id.glideButton)
        val udacityButton = findViewById<RadioButton>(R.id.udacityButton)
        val retroFitButton = findViewById<RadioButton>(R.id.retroFitButton)





        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        downloadButton.setOnClickListener {
            if(!glideButton.isChecked && !udacityButton.isChecked && !retroFitButton.isChecked){
                NotificationHelper.createToastNotification(this)
            }
           else{
                download()
           }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if(downloadID == id){
                val query = Query()
                query.setFilterById(downloadID)
                val cursor:Cursor = manager.query(query)
                if (cursor.moveToNext()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    cursor.close()
                    when (status) {
                        DownloadManager.STATUS_FAILED -> {
                           fileStatus = "failed"
                        }
                        DownloadManager.STATUS_PENDING, DownloadManager.STATUS_PAUSED -> {
                            fileStatus = "pending"
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            fileStatus = "successful"
                        }
                        DownloadManager.STATUS_RUNNING -> {
                            fileStatus = "running"
                        }
                    }
                }
                NotificationHelper.createDetailNotification(this@MainActivity,getString(R.string.notification_title),fileName,true,fileName,fileStatus)
            }
        }
    }

    private fun download() {
        if(udacityButton.isChecked){
            URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
            fileName = getString(R.string.loadApp)
        }
        else if (glideButton.isChecked){
            URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
            fileName = getString(R.string.glide)
            Log.e("glide","glideChecked")
        }
        else{ // RETROFIT
            URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"
            fileName = getString(R.string.retroFit)
        }
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        manager = downloadManager
    }

    companion object {
        private lateinit var URL:String
        private const val CHANNEL_ID = "channelId"
    }

}
