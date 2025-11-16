package com.example.prog7314poepart2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

object NotificationScheduler {

    fun scheduleTripNotifications(
        context: Context,
        tripName: String,
        startDateString: String
    ) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = sdf.parse(startDateString) ?: return

        val milestones = listOf(
            -7 to "Your trip starts in 7 days!",
            -3 to "Only 3 days left before your trip!",
            -1 to "Your trip starts tomorrow!",
            0 to "Your trip starts today!"
        )

        // Schedule all milestones
        for ((daysBefore, message) in milestones) {
            val calendar = Calendar.getInstance().apply {
                time = startDate
                add(Calendar.DAY_OF_YEAR, daysBefore)
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            if (calendar.timeInMillis > System.currentTimeMillis()) {
                setAlarm(context, tripName, message, calendar.timeInMillis)
            }
        }

        // Immediate notification showing days until trip
        val daysUntilTrip = ((startDate.time - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
        val immediateMessage = "Your trip '$tripName' is in $daysUntilTrip day${if (daysUntilTrip != 1) "s" else ""}!"
        setAlarm(context, tripName, immediateMessage, System.currentTimeMillis() + 1000) // 1 sec delay
    }

    private fun setAlarm(context: Context, tripName: String, message: String, time: Long) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("tripName", tripName)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (tripName + message).hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            // Explicit permission check for exact alarms (Android 12+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
                } else {
                    // fallback to normal alarm if exact alarms not allowed
                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
                }
            } else {
                // Older Android versions
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
        } catch (se: SecurityException) {
            se.printStackTrace()
        }
    }
}
