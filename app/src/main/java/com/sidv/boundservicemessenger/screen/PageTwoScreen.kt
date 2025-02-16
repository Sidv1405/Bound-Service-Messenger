package com.sidv.boundservicemessenger.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sidv.boundservicemessenger.service.TimerService
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@Composable
fun PageTwoScreen() {
    val activity = LocalContext.current as Activity
    var serviceMessenger by remember { mutableStateOf<Messenger?>(null) }
    var time by remember { mutableIntStateOf(0) }
    var isServiceConnected by remember { mutableStateOf(false) }


    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                serviceMessenger = Messenger(binder)
                isServiceConnected = true
                // Hiển thị Toast khi kết nối thành công
                Toast.makeText(activity, "Kết nối với Service thành công", Toast.LENGTH_SHORT).show()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                serviceMessenger = null
                isServiceConnected = false
                // Hiển thị Toast khi ngắt kết nối
                Toast.makeText(activity, "Đã ngắt kết nối với Service", Toast.LENGTH_SHORT).show()
            }
        }
    }

    DisposableEffect(Unit) {
        val intent = Intent(activity, TimerService::class.java)
        activity.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        onDispose {
            if (isServiceConnected) {
                activity.unbindService(connection)
                Log.d("PageTwoScreen", "PageTwoScreen: 111111")
            }
        }
    }

    val handler = remember {
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == TimerService.MSG_TIME_RESPONSE) {
                    time = msg.arg1
                }
            }
        }
    }
    val replyMessenger = Messenger(handler)

    LaunchedEffect(serviceMessenger) {
        while (serviceMessenger == null) {
            delay(500)
        }
        while (true) {
            val msg = Message.obtain(null, TimerService.MSG_GET_TIME)
            msg.replyTo = replyMessenger
            serviceMessenger?.send(msg)
            delay(1000)
        }
    }

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
            Text(text = "Page Two", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = connectionStatus, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Thời gian: $time giây", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
