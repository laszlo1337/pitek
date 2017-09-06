package io.finefabric.thewelder

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import java.util.*


/**
 * Created by laszlo on 2017-09-05.
 */
class AreYouAWelderYetService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private lateinit var timer: Timer

    private lateinit var pendingIntent: PendingIntent

    override fun onCreate() {
        super.onCreate()

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                spawaczNotify()
            }

        }, 0, 60000 * 60)

        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra("jid", "447455806680@s.whatsapp.net")
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra(Intent.EXTRA_TEXT, "Jesteś już spawaczem?")
        i.`package` = "com.whatsapp"
        pendingIntent = PendingIntent.getActivity(this, Notification.FLAG_AUTO_CANCEL, i, 0)

        val notification = NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setSmallIcon(R.mipmap.ic_launcher).setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setContentTitle("Spawacz service")
                .build()

        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT

        startForeground(SERVICE_ID, notification)

    }

    fun spawaczNotify() {

        val notification = NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentTitle("Napisz do pitka")
                .setContentIntent(pendingIntent).build()

        val screenLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG")
        screenLock.acquire(10000)

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        vibrator.vibrate(500)

        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(100, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onCreate()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    companion object {
        val SERVICE_ID = 1337
    }

}