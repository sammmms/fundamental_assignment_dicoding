package com.example.myfirstsubmission.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myfirstsubmission.R
import com.example.myfirstsubmission.data.retrofit.ApiConfig
import com.example.myfirstsubmission.data.retrofit.Event
import com.example.myfirstsubmission.data.retrofit.ListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    private var event : Event? = null
    private var result: Result = Result.success()

    override fun doWork(): Result {
        return getLatestEvent()
    }

    private fun getLatestEvent() : Result{
                val client = ApiConfig.getApiService().getLatestActiveEvents()
                client.enqueue(object: Callback<ListResponse> {
                    override fun onResponse(
                        call: Call<ListResponse>,
                        response: Response<ListResponse>
                    ) {
                        if(response.isSuccessful){
                            val listEvents = response.body()?.listEvents
                            Log.d("MyWorker", "onResponse: ${listEvents.toString()}")
                            if(!listEvents.isNullOrEmpty()){
                                event = listEvents[0]
                                showNotification(event as Event)
                            }
                            result = Result.success()
                        }
                    }

                    override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                        Log.e("MyWorker", "onFailure: ${t.message.toString()}")
                        result = Result.failure()
                    }
                })
        return result
    }

    private fun showNotification(event: Event){
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "channelId")
            .setSmallIcon(R.drawable.baseline_access_time_24)
            .setContentTitle(applicationContext.getString(R.string.notification_title, event.name))
            .setContentText(applicationContext.getString(R.string.notification_message, event.name, event.beginTime))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId("channelId")
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, notification.build())
    }
}