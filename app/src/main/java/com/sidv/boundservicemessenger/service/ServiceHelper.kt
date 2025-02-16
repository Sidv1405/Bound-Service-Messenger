//package com.sidv.boundservicemessenger.service
//
//import android.app.Activity
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.os.IBinder
//import android.util.Log
//import androidx.compose.runtime.*
//
//@Composable
//fun rememberBoundService(activity: Activity, screenName: String): TimerService? {
//    var service by remember { mutableStateOf<TimerService?>(null) }
//    val context = activity.applicationContext
//
//    val serviceConnection = remember {
//        object : ServiceConnection {
//            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
//                Log.d("ServiceHelper", "$screenName - onServiceConnected() - Đã kết nối với service")
//                val myBinder = binder as TimerService.TimerBinder
//                service = myBinder.getService()
//                service?.increaseConnectionCount(screenName) // Gọi hàm để đếm số màn hình kết nối
//            }
//
//            override fun onServiceDisconnected(name: ComponentName?) {
//                Log.d("ServiceHelper", "$screenName - onServiceDisconnected() - Service bị ngắt kết nối")
//                service = null
//            }
//        }
//    }
//
//    DisposableEffect(Unit) {
//        val intent = Intent(context, TimerService::class.java)
//        Log.d("ServiceHelper", "$screenName - Binding service...")
//        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
//
//        onDispose {
//            Log.d("ServiceHelper", "$screenName - Unbinding service...")
//            service?.decreaseConnectionCount(screenName) // Gọi hàm để giảm số màn hình kết nối
//            context.unbindService(serviceConnection)
//        }
//    }
//
//    return service
//}
//
