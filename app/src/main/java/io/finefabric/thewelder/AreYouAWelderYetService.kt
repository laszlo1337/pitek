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

        }, 0, AlarmManager.INTERVAL_DAY)

        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra("jid", "XXX@s.whatsapp.net")
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra(Intent.EXTRA_TEXT, "jesteś już spawaczem?")
        i.`package` = "com.whatsapp"

        pendingIntent = PendingIntent.getActivity(this, Notification.FLAG_ONGOING_EVENT, i, 0)
    }

    fun spawaczNotify() {

        val notification = NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setStyle(NotificationCompat.BigTextStyle().bigText("pitek musi być spawaczę"))
                .setContentTitle("Spawacz")
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
        return START_STICKY
    }

    companion object {
        val SERVICE_ID = 1337
    }

}