package com.sidv.boundservicemessenger.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import kotlinx.coroutines.*

class TimerService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var elapsedTime = 0
    private var job: Job? = null

    companion object {
        const val MSG_GET_TIME = 1
        const val MSG_TIME_RESPONSE = 2
    }

    private val serviceHandler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            MSG_GET_TIME -> {
                val replyMessenger = msg.replyTo
                val replyMsg = Message.obtain(null, MSG_TIME_RESPONSE, elapsedTime, 0)
                Log.d("zzzzzz", "TimerService: Nhận MSG_GET_TIME từ Activity, gửi lại(MSG_TIME_RESPONSE) elapsedTime = $elapsedTime")
                replyMessenger.send(replyMsg) // Gửi thời gian về Activity
            }
        }
        true
    }
    private val messenger = Messenger(serviceHandler)

    override fun onCreate() {
        super.onCreate()
        startTimer()
    }

    override fun onBind(intent: Intent?): IBinder {
        return messenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun startTimer() {
        job?.cancel()
        job = coroutineScope.launch {
            while (isActive) {
                delay(1000)
                elapsedTime++
//                Log.d("zzzzzz", "TimerService: elapsedTime tăng lên $elapsedTime")
            }
        }
    }
}
