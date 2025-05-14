package com.example.greenalert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ProcessReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val processName = inputData.getString("processName") ?: return Result.failure()
        val indicatorName = inputData.getString("indicatorName") ?: ""
        showNotification(processName, indicatorName)
        return Result.success()
    }

    private fun showNotification(processName: String, indicatorName: String) {
        val channelId = "process_reminder_channel"
        val notificationId = processName.hashCode() + indicatorName.hashCode()
        val title = applicationContext.getString(R.string.app_name)
        val message = "It's time to repeat the process: $processName for $indicatorName."

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Process Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.soil)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }
} 