import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

object NotificationHelper {
    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //create a new notification channel
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId,name,importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }

    fun createDetailNotification(
        context: Context,
        title:String,
        description: String,
        autoCancel:Boolean,
        fileName:String,
        fileStatus:String
    ){
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(context,channelId).apply{
            setSmallIcon(R.drawable.ic_assistant_black_24dp)
            setContentTitle(title)
            setContentText(description)
            priority = NotificationCompat.PRIORITY_DEFAULT

            val pendingIntent: PendingIntent?
            val intent = Intent(context, DetailActivity::class.java)

            intent.putExtra("fileName", fileName)
            intent.putExtra("fileStatus",fileStatus)

            intent.action = context.getString(R.string.status)
            pendingIntent =
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT
                )

            addAction(R.drawable.ic_launcher_background,context.getString(R.string.status),pendingIntent)
            setContentIntent(pendingIntent)
            setAutoCancel(autoCancel)
        }


        //notify
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001,notificationBuilder.build())

    }

    fun createToastNotification(
        context: Context
    ){
        Toast.makeText(context,R.string.please,Toast.LENGTH_SHORT).show()
    }
}