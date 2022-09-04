package com.example.farmmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) { //토큰을 생성하는 메서드
        Log.d("토큰","$token")
        //logcat을 통해 토큰 ID 검색가능
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
        // 토큰 값을 따로 저장해둔다.
        Log.i("로그: ", "성공적으로 토큰을 저장함")
    }

}
