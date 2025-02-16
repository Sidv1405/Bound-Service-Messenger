package com.sidv.boundservicemessenger.screen

import android.annotation.SuppressLint
import androidx.compose.runtime.*

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sidv.boundservicemessenger.service.TimerService
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@Composable
fun PageOneScreen() {
    val activity = LocalContext.current as Activity
    var serviceMessenger by remember { mutableStateOf<Messenger?>(null) }
    var time by remember { mutableIntStateOf(0) }
    var isServiceConnected by remember { mutableStateOf(false) } // Trạng thái kết nối với Service

    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                serviceMessenger = Messenger(binder)
                Log.d("zzzzzz", "PageOneScreen: Đã kết nối với TimerService")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                serviceMessenger = null
                Log.d("zzzzzz", "PageOneScreen: Mất kết nối với TimerService")
            }
        }
    }

    DisposableEffect(Unit) {
        val intent = Intent(activity, TimerService::class.java)
        val bound = activity.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Log.d("zzzzzz", "PageOneScreen: Gọi bindService() - Kết quả: $bound")

        onDispose {
            activity.unbindService(connection)
            Log.d("zzzzzz", "PageOneScreen: Gọi unbindService()")
        }
    }

    // Handler để nhận phản hồi từ Service
    val handler = remember {
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == TimerService.MSG_TIME_RESPONSE) {
                    time = msg.arg1
                    Log.d("zzzzzz", "PageOneScreen: Nhận phản hồi từ Service(MSG_TIME_RESPONSE) - elapsedTime = ${msg.arg1}")
                }
            }
        }
    }
    val replyMessenger = Messenger(handler)

    LaunchedEffect(serviceMessenger) {
        while (serviceMessenger == null) {
            delay(100)
        }
        Log.d("zzzzzz", "PageOneScreen: Messenger đã kết nối, bắt đầu gửi tin nhắn MSG_GET_TIME")

        while (true) {
            val msg = Message.obtain(null, TimerService.MSG_GET_TIME)
            msg.replyTo = replyMessenger
            serviceMessenger?.send(msg)
            Log.d("zzzzzz", "PageOneScreen: Gửi MSG_GET_TIME đến TimerService")
            delay(1000)
        }
    }

    // Hiển thị trạng thái kết nối với Service
    val connectionStatus = if (isServiceConnected) {
        "Đã kết nối với Service"
    } else {
        "Đang kết nối với Service..."
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Page One", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = connectionStatus, style = MaterialTheme.typography.bodyLarge) // Hiển thị trạng thái kết nối
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Thời gian: $time giây", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
